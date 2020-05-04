package org.iitbact.cc.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.iitbact.cc.constants.Constants;
import org.iitbact.cc.constants.LinkingStatus;
import org.iitbact.cc.dto.AvailabilityStatus;
import org.iitbact.cc.dto.FacilityDto;
import org.iitbact.cc.entities.AdminUser;
import org.iitbact.cc.entities.Facility;
import org.iitbact.cc.entities.FacilityLink;
import org.iitbact.cc.exceptions.CovidControlErpError;
import org.iitbact.cc.exceptions.CovidControlErrorCode;
import org.iitbact.cc.exceptions.CovidControlErrorMsg;
import org.iitbact.cc.exceptions.CovidControlException;
import org.iitbact.cc.repository.FacilityLinkRepository;
import org.iitbact.cc.repository.FacilityRepository;
import org.iitbact.cc.repository.WardRepository;
import org.iitbact.cc.requests.FacilityRequest;
import org.iitbact.cc.requests.FacilitySearchCriteria;
import org.iitbact.cc.requests.LinkFacilitiesRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FacilityServices {

	private static final Supplier<CovidControlException> facilityDoesNotExistException = () -> new CovidControlException(
			new CovidControlErpError(CovidControlErrorCode.FACILITY_DOES_NOT_EXIST,
					CovidControlErrorMsg.FACILITY_DOES_NOT_EXIST));

	private final FacilityRepository facilityRepository;
	private final FacilityLinkRepository facilityLinkRepository;
	private final UserServices userServices;
	private final WardRepository wardRepository;

	public FacilityServices(FacilityRepository facilityRepository, FacilityLinkRepository facilityLinkRepository, UserServices userServices, WardRepository wardRepository) {
		this.facilityRepository = facilityRepository;
		this.facilityLinkRepository = facilityLinkRepository;
        this.userServices = userServices;
        this.wardRepository = wardRepository;
	}

	@Transactional
	public FacilityDto createFacility(FacilityRequest request,String uid) throws CovidControlException {
		Facility facility = request.getFacilityProfile();

		AdminUser user=userServices.profile(uid);
		facility.setRegion(user.getRegion());

		log.info("Create Facility Request");
		log.debug("Create Facility Request - {}", facility.toString());

		if(null != facility.getFacilityId()){
			throw new CovidControlException(new CovidControlErpError(CovidControlErrorCode.FACILITY_ID_SHOULD_BE_NULL, CovidControlErrorMsg.FACILITY_ID_SHOULD_BE_NULL));
		}
		facility.getFacilityContact().setFacility(facility);
		facilityRepository.save(facility);

		log.info("Facility created successfully with id {}", request.getFacilityProfile().getFacilityId());
		return toFacilityDto(request.getFacilityProfile());
	}

	public FacilityDto editFacility(int facilityId, FacilityRequest facilityRequest,String uid) throws CovidControlException {
		log.info("Edit Facility Request - facility Id {}", facilityId);
		log.debug("Facility Edit Request Content {}", facilityRequest.toString());
		AdminUser user=userServices.profile(uid);


		Facility facility = facilityRepository.findById(facilityId).orElseThrow(facilityDoesNotExistException);
		facility.copy(facilityRequest.getFacilityProfile(),user);

		facility.getFacilityContact().setFacility(facility);
		facilityRepository.save(facility);
		log.info("Facility {} updated successfully", facilityId);
		return fetchAvailabilityStatusConvertToDto(facility);
	}

	public FacilityDto fetchFacilityData(int facilityId) throws CovidControlException {
		log.info("Fetch Facility Data request {}", facilityId);

		return fetchAvailabilityStatusConvertToDto(facilityRepository.findById(facilityId).orElseThrow(facilityDoesNotExistException));
	}

	public Boolean linkFacilities(int facilityId, LinkFacilitiesRequest linkFacilitiesRequest)
			throws CovidControlException {
		log.info("Link Facilities Request. Facility Id - {}, Facilities to be linked {}, Facilities to be de-linked {}",
				facilityId,
				linkFacilitiesRequest.getFacilityLinks().stream()
						.filter(link -> link.getLinkingStatus() == LinkingStatus.LINK)
						.map(LinkFacilitiesRequest.Link::getFacilityId).collect(Collectors.toList()),
				linkFacilitiesRequest.getFacilityLinks().stream()
						.filter(link -> link.getLinkingStatus() == LinkingStatus.UNLINK)
						.map(LinkFacilitiesRequest.Link::getFacilityId).collect(Collectors.toList()));

		Facility facility = facilityRepository.findById(facilityId).orElseThrow(facilityDoesNotExistException);
		for (LinkFacilitiesRequest.Link link : linkFacilitiesRequest.getFacilityLinks()) {
			if (link.getLinkingStatus() == LinkingStatus.LINK) {
				addBidirectionalLink(facilityId, link.getFacilityId());
			} else if (link.getLinkingStatus() == LinkingStatus.UNLINK) {
				removeBidirectionalLink(facilityId, link.getFacilityId());
			}
			updateHasLinksField(facilityId, link.getFacilityId());
		}
		return true;
	}

	public List<FacilityDto> getLinkedFacilities(int facilityId) throws CovidControlException {
		log.info("Get Linked Facilities - facility id {}", facilityId);

		Facility facility = facilityRepository.findById(facilityId).orElseThrow(facilityDoesNotExistException);

		return fetchAvailabilityStatusConvertToDto(facilityLinkRepository.getAllBySourceFacilityId(facilityId)
				.stream()
				.map(FacilityLink::getMappedFacilty)
				.collect(Collectors.toList()));
	}

	private void addBidirectionalLink(Integer facilityId1, Integer facilityId2) {
		addSingleLink(facilityId1, facilityId2);
		addSingleLink(facilityId2, facilityId1);
	}

	private void updateHasLinksField(Integer facilityId1, Integer facilityId2) {
		updateHasLinksField(facilityId1);
		updateHasLinksField(facilityId2);
	}

	private void updateHasLinksField(Integer facilityId) {
		Boolean hasLinks = facilityLinkRepository.existsBySourceFacilityId(facilityId);
		log.info("Setting has_links to {} for facility Id {}", hasLinks, facilityId);
		Facility facility = facilityRepository.getOne(facilityId);
		facility.setHasLinks(hasLinks);
		facilityRepository.save(facility);
	}

	private void addSingleLink(Integer sourceFacilityId, Integer mappedFacilityId) {
		log.info("Adding link for {} - {}", sourceFacilityId, mappedFacilityId);
		Boolean linkAlreadyExists = facilityLinkRepository.existsBySourceFacilityIdAndMappedFacilityId(sourceFacilityId,
				mappedFacilityId);
		if (!linkAlreadyExists) {
			if (facilityRepository.existsById(mappedFacilityId)) {
				FacilityLink facilityLink = FacilityLink.builder().sourceFacilityId(sourceFacilityId)
						.mappedFacilityId(mappedFacilityId).build();
				facilityLinkRepository.save(facilityLink);
				log.info("Link created successfully for {} and {}", sourceFacilityId, mappedFacilityId);
			} else {
				log.error("No facility exists by id {}. Invalid link.", mappedFacilityId);
			}
		} else {
			log.error("Link already exists between {} and {}", sourceFacilityId, mappedFacilityId);
		}
	}

	private void removeBidirectionalLink(Integer facilityId1, Integer facilityId2) {
		removeSingleLink(facilityId1, facilityId2);
		removeSingleLink(facilityId2, facilityId1);
	}

	private void removeSingleLink(Integer sourceFacilityId, Integer mappedFacilityId) {
		Optional<FacilityLink> facilityLink = facilityLinkRepository
				.getBySourceFacilityIdAndMappedFacilityId(sourceFacilityId, mappedFacilityId);
		if (facilityLink.isPresent()) {
			facilityLinkRepository.delete(facilityLink.get());
			log.info("Link deleted successfully for {} and {}", sourceFacilityId, mappedFacilityId);
		} else {
			log.error("Link does not exist between {} and {}", sourceFacilityId, mappedFacilityId);
		}
	}

	public List<FacilityDto> getFacilities(int pageNo, String uid, FacilitySearchCriteria searchCriteria) throws CovidControlException {
		AdminUser user=userServices.profile(uid);
		Pageable pageRequest = PageRequest.of(pageNo - 1, Constants.PAGE_SIZE);

		Page<Facility> page = facilityRepository.findAll(FacilitySearchSpecificaton.findByCriteria(searchCriteria,user.getRegion()),
				pageRequest);

		return fetchAvailabilityStatusConvertToDto(page.toList());
	}

	private static class FacilitySearchSpecificaton {
		private static Specification<Facility> findByCriteria(final FacilitySearchCriteria searchCriteria,int region) {
			return new Specification<Facility>() {
				private static final long serialVersionUID = 1L;

				@Override
				public Predicate toPredicate(Root<Facility> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					List<Predicate> predicates = new ArrayList<>();

					Predicate predicateForRegion = cb.equal(root.get("region"),region);
					predicates.add(predicateForRegion);

					if(searchCriteria.getName() != null && !searchCriteria.getName().isEmpty()) {
						Predicate predicateForName = cb.like(cb.lower(root.get("name")), "%" + searchCriteria.getName().toLowerCase() + "%");
						predicates.add(predicateForName);
					}

					if(searchCriteria.getHasLinks() != null){
						predicates.add(cb.equal(root.get("hasLinks"), searchCriteria.getHasLinks()));
					}

					if (searchCriteria.getAreas() != null && !searchCriteria.getAreas().isEmpty()) {
						predicates.add(root.get("area").in(searchCriteria.getAreas()));
					}
					if (searchCriteria.getCovidFacilityType() != null
							&& !searchCriteria.getCovidFacilityType().isEmpty()) {
						predicates.add(root.get("covidFacilityType").in(searchCriteria.getCovidFacilityType()));
					}
					if (searchCriteria.getJurisdictions() != null && !searchCriteria.getJurisdictions().isEmpty()) {
						predicates.add(root.get("jurisdiction").in(searchCriteria.getJurisdictions()));
					}
					if (searchCriteria.getFacilityStatus() != null && !searchCriteria.getFacilityStatus().isEmpty()) {
						predicates.add(root.get("facilityStatus").in(searchCriteria.getFacilityStatus()));
					}


					return cb.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};
		}
	}


	private FacilityDto toFacilityDto(Facility facility){
		return FacilityDto.createFromFacility(facility);
	}

	private FacilityDto fetchAvailabilityStatusConvertToDto(Facility facility){
		List<AvailabilityStatus> availabilityStatusList = wardRepository.getAvailabilityStatus(Collections.singletonList(facility.getFacilityId()));
		return FacilityDto.createFromFacility(facility, availabilityStatusList);
	}

	private List<FacilityDto> toFacilityDto(List<Facility> facilities){
		return facilities.stream().map(FacilityDto::createFromFacility).collect(Collectors.toList());
	}

	private List<FacilityDto> fetchAvailabilityStatusConvertToDto(List<Facility> facilities){
		List<Integer> facilityIds = facilities.stream().map(Facility::getFacilityId).collect(Collectors.toList());
		List<AvailabilityStatus> availabilityStatusList = wardRepository.getAvailabilityStatus(facilityIds);
		return merge(facilities, availabilityStatusList);

	}

	private List<FacilityDto> merge(List<Facility> facilities, List<AvailabilityStatus> availabilityStatusList) {
		Map<Integer, Facility> facilityMap = facilities.stream().collect(Collectors.toMap(Facility::getFacilityId, facility -> facility));
		Map<Integer, List<AvailabilityStatus>> availabilityStatusMap = new HashMap<>();
		for (AvailabilityStatus availabilityStatus : availabilityStatusList){
			availabilityStatusMap.computeIfAbsent(availabilityStatus.getFacilityId(), (id) -> new LinkedList<>())
					.add(availabilityStatus);
		}
		List<FacilityDto> facilityDtos = new LinkedList<>();
		for(Map.Entry<Integer, Facility> facilityEntry: facilityMap.entrySet()){
			Integer facilityId = facilityEntry.getKey();
			Facility facility = facilityEntry.getValue();
			List<AvailabilityStatus> availabilityStatus = availabilityStatusMap.getOrDefault(facilityId, Collections.emptyList());

			FacilityDto facilityDto = FacilityDto.createFromFacility(facility, availabilityStatus);

			facilityDtos.add(facilityDto);
		}
		return facilityDtos;
	}
}
