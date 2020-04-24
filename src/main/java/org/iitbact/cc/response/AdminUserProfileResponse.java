package org.iitbact.cc.response;

import lombok.Data;
import org.iitbact.cc.beans.BaseBean;
import org.iitbact.cc.entities.AdminUser;

@Data
public class AdminUserProfileResponse implements BaseBean<AdminUser> {

    private AdminUser profile;

    @Override
    public void setEntity(AdminUser profile) {
        this.profile = profile;
    }
}
