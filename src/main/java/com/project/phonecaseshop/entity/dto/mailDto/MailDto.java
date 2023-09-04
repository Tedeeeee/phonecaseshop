package com.project.phonecaseshop.entity.dto.mailDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailDto {
    private String address;
    private String title;
    private String message;
    private int memberId;
    private String changePassword;
}
