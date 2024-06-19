package com.eco.environet.education.model.compositeKeys;

import com.eco.environet.education.model.Lecture;
import com.eco.environet.users.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@Data
public class TestExecutionId implements Serializable {
    private User user;
    private Lecture lecture;
}
