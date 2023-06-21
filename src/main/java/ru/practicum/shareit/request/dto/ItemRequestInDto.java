package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestInDto {
    private String description;

    public String getDescription() {
        if (description == null) return "";
        return description;
    }
}
