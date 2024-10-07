package com.goodquestion.edutrek_server.modules.lecturer.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LecturerDataDto {

    @NotBlank(message = "Lecturer name cannot be empty")
    @Size(min = 2, max = 100, message = "Lecturer name must be between 2 and 100 characters")
    private String lecturerName;

    @NotNull(message = "Phone number cannot be null")
    @Pattern(regexp = "^(?:\\+972|0)([23489]|5[0-9])-?\\d{7}$",
            message = "Phone number must be a valid Israeli number")
    private String phone;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "Branch ID cannot be null")
    private int branchId;

    @Size(max = 255, message = "Comment must be less than 255 characters")
    private String comment;
}