package com.practice.employee;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(MockitoExtension.class)
public abstract class UnitTest {
  protected MockMvc mockMvc;
  protected final String BASE_PATH = "/api/employee";
}
