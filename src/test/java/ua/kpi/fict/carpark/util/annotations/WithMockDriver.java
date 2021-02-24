package ua.kpi.fict.carpark.util.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.security.test.context.support.WithMockUser;

@Retention(RetentionPolicy.RUNTIME)
@WithMockUser(value = "karimlegoida", roles = {"DRIVER"})
public @interface WithMockDriver {
}
