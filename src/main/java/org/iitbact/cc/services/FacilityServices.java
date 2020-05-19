package org.iitbact.cc.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.iitbact.cc.beans.PageInfo;
import org.iitbact.cc.constants.LinkingStatus;
import org.iitbact.cc.dto.AvailabilityStatus;
import org.iitbact.cc.dto.FacilityDto;
import org.iitbact.cc.dto.FacilityLinkWrapperDto;
import org.iitbact.cc.dto.LinkCount;
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

import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FacilityServices {

	private final FacilityRepository facilityRepository;
	private final FacilityLinkRepository facilityLinkRepository;
	private final UserServices userServices;
	private final WardRepository wardRepository;
	private final ApiValidationService validationService;
	private final FirebaseUserMgmt firebaseUserMgmt;

	public FacilityServices(FacilityRepository facilityRepository, FacilityLinkRepository facilityLinkRepository,
			UserServices userServices, WardRepository wardRepository, ApiValidationService validationService,FirebaseUserMgmt firebaseUserMgmt) {
		this.facilityRepository = facilityRepository;
		this.facilityLinkRepository = facilityLinkRepository;
		this.userServices = userServices;
		this.wardRepository = wardRepository;
		this.validationService = validationService;
		this.firebaseUserMgmt=firebaseUserMgmt;
	}

	@Transactional
	public FacilityDto createFacility(FacilityRequest request, String uid) throws CovidControlException {
		Facility facility = request.getFacilityProfile();

		AdminUser user = userServices.profile(uid);
		facility.setRegion(user.getRegion());

		log.info("Create Facility Request");
		log.debug("Create Facility Request - {}", facility.toString());

		if (null != facility.getFacilityId()) {
			throw new CovidControlException(new CovidControlErpError(CovidControlErrorCode.FACILITY_ID_SHOULD_BE_NULL,
					CovidControlErrorMsg.FACILITY_ID_SHOULD_BE_NULL));
		}
		setAllExtraFields(facility);
		facilityRepository.save(facility);
		
		log.info("Facility created successfully with id {}", request.getFacilityProfile().getFacilityId());
		return toFacilityDto(facility);
	}

	public FacilityDto editFacility(int facilityId, FacilityRequest facilityRequest, String uid)
			throws CovidControlException {
		log.info("Edit Facility Request - facility Id {}", facilityId);
		log.debug("Facility Edit Request Content {}", facilityRequest.toString());
		Facility facility = validationService.facilityValidation(uid, facilityId);
		
		String oldEmail=facility.getEmail();
		
		facility.copy(facilityRequest.getFacilityProfile());

		facilityRepository.save(facility);
		log.info("Facility {} updated successfully", facilityId);
		
		firebaseUserMgmt.updateEmailId(oldEmail, facility.getEmail());
		
		return fetchAvailabilityStatusAndLinkCountConvertToDto(facility);
	}

	private void setAllExtraFields(Facility facility) {
		if (facility.getFacilityContact() != null) {
			facility.getFacilityContact().setFacility(facility);
		}
		if (facility.getFacilityAssets() != null) {
			facility.getFacilityAssets().setFacility(facility);
		}
		if (facility.getFacilityChecklist() != null) {
			facility.getFacilityChecklist().setFacility(facility);
		}
		if (facility.getFacilityMedstaff() != null) {
			facility.getFacilityMedstaff().setFacility(facility);
		}
		if (facility.getFacilityInventory() != null) {
			facility.getFacilityInventory().setFacility(facility);
		}
	}

	public FacilityDto fetchFacilityData(String uid,int facilityId) throws CovidControlException {
		log.info("Fetch Facility Data request {}", facilityId);
		Facility facility=validationService.facilityValidation(uid, facilityId);
		return fetchAvailabilityStatusAndLinkCountConvertToDto(facility);
	}

	public Boolean linkFacilities(String uid,int facilityId, LinkFacilitiesRequest linkFacilitiesRequest)
			throws CovidControlException {
		log.info("Link Facilities Request. Facility Id - {}, Facilities to be linked {}, Facilities to be de-linked {}",
				facilityId,
				linkFacilitiesRequest.getFacilityLinks().stream()
						.filter(link -> link.getLinkingStatus() == LinkingStatus.LINK)
						.map(LinkFacilitiesRequest.Link::getFacilityId).collect(Collectors.toList()),
				linkFacilitiesRequest.getFacilityLinks().stream()
						.filter(link -> link.getLinkingStatus() == LinkingStatus.UNLINK)
						.map(LinkFacilitiesRequest.Link::getFacilityId).collect(Collectors.toList()));
		
		//TODO check linked facility Ids as well
		validationService.facilityValidation(uid, facilityId);
		
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

	public FacilityLinkWrapperDto getLinkedFacilities(String uid, int facilityId) throws CovidControlException {
		log.info("Get Linked Facilities - facility id {}", facilityId);

		validationService.facilityValidation(uid, facilityId);
		
		String DCH = "DCH";
		String DCHC = "DCHC";
		String CCC = "CCC";

		String SEVERE = "SEVERE";
		String MODERATE = "MODERATE";
		String MILD = "MILD";

		List<FacilityDto> DCHFacilities = fetchAvailabilityStatusAndLinkCountConvertToDto(
				facilityLinkRepository.getAllBySourceFacilityIdAndCovidFacilityType(facilityId, DCH).stream()
						.map(FacilityLink::getMappedFacilty).collect(Collectors.toList()),
				SEVERE);

		List<FacilityDto> DCHCFacilities = fetchAvailabilityStatusAndLinkCountConvertToDto(
				facilityLinkRepository.getAllBySourceFacilityIdAndCovidFacilityType(facilityId, DCHC).stream()
						.map(FacilityLink::getMappedFacilty).collect(Collectors.toList()),
				MODERATE);

		List<FacilityDto> CCCFacilities = fetchAvailabilityStatusAndLinkCountConvertToDto(
				facilityLinkRepository.getAllBySourceFacilityIdAndCovidFacilityType(facilityId, CCC).stream()
						.map(FacilityLink::getMappedFacilty).collect(Collectors.toList()),
				MILD);

		return FacilityLinkWrapperDto.builder().DCHFacilities(DCHFacilities).DCHCFacilities(DCHCFacilities)
				.CCCFacilities(CCCFacilities).build();
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
		List<String> typesOfWards = wardRepository.getTypesOfWards(mappedFacilityId);
		for (String typeOfWard : typesOfWards) {
			String covidStatus = covertSeverityToCovidStatus(typeOfWard);
			if (covidStatus != null) {
				Boolean linkAlreadyExists = facilityLinkRepository
						.existsBySourceFacilityIdAndMappedFacilityIdAndCovidFacilityType(sourceFacilityId,
								mappedFacilityId, covidStatus);
				if (!linkAlreadyExists) {
					if (facilityRepository.existsById(mappedFacilityId)) {
						FacilityLink facilityLink = FacilityLink.builder().sourceFacilityId(sourceFacilityId)
								.mappedFacilityId(mappedFacilityId).covidFacilityType(covidStatus).build();
						facilityLinkRepository.save(facilityLink);
						log.info("Link created successfully for {} and {} with covid status {}", sourceFacilityId,
								mappedFacilityId, covidStatus);
					} else {
						log.error("No facility exists by id {}. Invalid link.", mappedFacilityId);
					}
				} else {
					log.error("Link already exists between {} and {} with covid status {}", sourceFacilityId,
							mappedFacilityId, covidStatus);
				}
			} else {
				log.error("No mapping exists for severity {}", typeOfWard);
			}
		}
	}

	private void removeBidirectionalLink(Integer facilityId1, Integer facilityId2) {
		removeSingleLink(facilityId1, facilityId2);
		removeSingleLink(facilityId2, facilityId1);
	}

	private void removeSingleLink(Integer sourceFacilityId, Integer mappedFacilityId) {
		List<FacilityLink> facilityLinks = facilityLinkRepository
				.findAllBySourceFacilityIdAndMappedFacilityId(sourceFacilityId, mappedFacilityId);
		if (!facilityLinks.isEmpty()) {
			for (FacilityLink facilityLink : facilityLinks) {
				facilityLinkRepository.delete(facilityLink);
				log.info("Link deleted successfully for {} and {} with covid status {}", sourceFacilityId,
						mappedFacilityId, facilityLink.getCovidFacilityType());
			}
		} else {
			log.error("Link does not exist between {} and {}", sourceFacilityId, mappedFacilityId);
		}
	}

	public org.iitbact.cc.beans.Page<FacilityDto> getFacilities(int offset, int limit, String uid,
			FacilitySearchCriteria searchCriteria) throws CovidControlException {
		AdminUser user = userServices.profile(uid);
		Pageable pageRequest = PageRequest.of(offset, limit);

		Page<Facility> page = facilityRepository
				.findAll(FacilitySearchSpecificaton.findByCriteria(searchCriteria, user.getRegion()), pageRequest);
		List<FacilityDto> list = fetchAvailabilityStatusAndLinkCountConvertToDto(page.toList());

		org.iitbact.cc.beans.Page<FacilityDto> slice = new org.iitbact.cc.beans.Page<FacilityDto>();

		slice.setElements(list);
		slice.setMeta(new PageInfo(limit, offset, page.getTotalElements()));
		return slice;
	}

	private static class FacilitySearchSpecificaton {
		private static Specification<Facility> findByCriteria(final FacilitySearchCriteria searchCriteria, int region) {
			return new Specification<Facility>() {
				private static final long serialVersionUID = 1L;

				@Override
				public Predicate toPredicate(Root<Facility> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					List<Predicate> predicates = new ArrayList<>();

					Predicate predicateForRegion = cb.equal(root.get("region"), region);
					predicates.add(predicateForRegion);

					if (searchCriteria.getName() != null && !searchCriteria.getName().isEmpty()) {
						Predicate predicateForName = cb.like(cb.lower(root.get("name")),
								"%" + searchCriteria.getName().toLowerCase() + "%");
						predicates.add(predicateForName);
					}

					if (searchCriteria.getHasLinks() != null) {
						predicates.add(cb.equal(root.get("hasLinks"), searchCriteria.getHasLinks()));
					}

					if (searchCriteria.getOperatingStatus() != null) {
						predicates.add(cb.equal(root.get("operatingStatus"), searchCriteria.getOperatingStatus()));
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

	private FacilityDto toFacilityDto(Facility facility) {
		return FacilityDto.createFromFacility(facility);
	}

	private FacilityDto fetchAvailabilityStatusAndLinkCountConvertToDto(Facility facility) {
		List<AvailabilityStatus> availabilityStatusList = wardRepository
				.getAvailabilityStatus(Collections.singletonList(facility.getFacilityId()));
		LinkCount linkCount = facilityLinkRepository.findLinkCount(facility.getFacilityId());
		return FacilityDto.createFromFacility(facility, availabilityStatusList, linkCount);
	}

	private List<FacilityDto> fetchAvailabilityStatusAndLinkCountConvertToDto(List<Facility> facilities) {
		List<Integer> facilityIds = facilities.stream().map(Facility::getFacilityId).collect(Collectors.toList());
		List<AvailabilityStatus> availabilityStatusList = wardRepository.getAvailabilityStatus(facilityIds);
		List<LinkCount> linkCounts = facilityLinkRepository.findLinkCount(facilityIds);
		return merge(facilities, availabilityStatusList, linkCounts);
	}

	private List<FacilityDto> fetchAvailabilityStatusAndLinkCountConvertToDto(List<Facility> facilities,
			String covidStatus) {
		List<Integer> facilityIds = facilities.stream().map(Facility::getFacilityId).collect(Collectors.toList());
		List<AvailabilityStatus> availabilityStatusList = wardRepository.getAvailabilityStatus(facilityIds,
				covidStatus);
		List<LinkCount> linkCounts = facilityLinkRepository.findLinkCount(facilityIds);
		return merge(facilities, availabilityStatusList, linkCounts);
	}

	private List<FacilityDto> merge(List<Facility> facilities, List<AvailabilityStatus> availabilityStatusList,
			List<LinkCount> linkCounts) {
		Map<Integer, Facility> facilityMap = facilities.stream()
				.collect(Collectors.toMap(Facility::getFacilityId, facility -> facility));
		Map<Integer, List<AvailabilityStatus>> availabilityStatusMap = new HashMap<>();
		Map<Integer, LinkCount> linkCountMap = new HashMap<>();
		for (AvailabilityStatus availabilityStatus : availabilityStatusList) {
			availabilityStatusMap.computeIfAbsent(availabilityStatus.getFacilityId(), (id) -> new LinkedList<>())
					.add(availabilityStatus);
		}
		for (LinkCount linkCount : linkCounts) {
			linkCountMap.put(linkCount.getFacilityId(), linkCount);
		}

		List<FacilityDto> facilityDtos = new LinkedList<>();
		for (Map.Entry<Integer, Facility> facilityEntry : facilityMap.entrySet()) {
			Integer facilityId = facilityEntry.getKey();
			Facility facility = facilityEntry.getValue();
			List<AvailabilityStatus> availabilityStatus = availabilityStatusMap.getOrDefault(facilityId,
					Collections.emptyList());
			LinkCount linkCount = linkCountMap.get(facilityId);

			FacilityDto facilityDto = FacilityDto.createFromFacility(facility, availabilityStatus, linkCount);

			facilityDtos.add(facilityDto);
		}
		return facilityDtos;
	}

	private String covertSeverityToCovidStatus(String severity) {
		if ("MILD".equals(severity)) {
			return "CCC";
		} else if ("MODERATE".equals(severity)) {
			return "DCHC";
		} else if ("SEVERE".equals(severity)) {
			return "DCH";
		}
		return null;
	}

	public String generateCredentails(String uid, int facilityId) throws CovidControlException {
		Facility facility = validationService.facilityValidation(uid, facilityId);
		String pLink= firebaseUserMgmt.createUpdateUser(facility);
		return pLink;
	}

	public UserRecord facilityUserProfile(String uid, int facilityId) throws CovidControlException {
		Facility facility= validationService.facilityValidation(uid,facilityId);
		return firebaseUserMgmt.fetchUser(facility.getEmail());
	}
	
	public UserRecord addHasuraClaimsToExistingUsers(String uid, int facilityId) throws CovidControlException {
		Facility facility= validationService.facilityValidation(uid,facilityId);
		return firebaseUserMgmt.addHasuraClaims(facility);
	}

	public FirebaseToken decodeToken(String token) throws CovidControlException {
		return firebaseUserMgmt.checkJwt(token);
	}
}
