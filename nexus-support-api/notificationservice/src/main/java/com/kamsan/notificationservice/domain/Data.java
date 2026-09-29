package com.kamsan.notificationservice.domain;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Data {
    private String name;
    private String email;
    private String token;
    private String ticketTitle;
    private String ticketNumber;
    private String priority;
    private String comment;
    private String date;
    private String files;
}
