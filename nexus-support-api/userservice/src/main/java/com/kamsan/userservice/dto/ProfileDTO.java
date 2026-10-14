package com.kamsan.userservice.dto;

import java.util.List;

public record ProfileDTO(
        ReadUserDTO user,
        List<DeviceDTO> devices) {
}
