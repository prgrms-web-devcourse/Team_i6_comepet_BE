package com.pet.domains.account.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NotificationUpdateParam {

    private boolean checked;

    public NotificationUpdateParam(boolean checked) {
        this.checked = checked;
    }

}
