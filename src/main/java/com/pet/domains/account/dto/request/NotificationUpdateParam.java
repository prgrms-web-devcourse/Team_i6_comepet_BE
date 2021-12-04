package com.pet.domains.account.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotificationUpdateParam {

    private boolean checked;

    public NotificationUpdateParam(boolean checked) {
        this.checked = checked;
    }

}
