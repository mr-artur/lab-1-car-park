package ua.kpi.fict.carpark.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import ua.kpi.fict.carpark.entity.enums.Role;
import ua.kpi.fict.carpark.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    List<User> findByRole(Role role);

    List<User> findByEnabledIsFalse();

    Page<User> findByEnabledIsFalse(Pageable pageable);

    @Modifying
    @Query("UPDATE User u "
        + "SET u.accountNonExpired = true, "
        + "u.accountNonLocked = true, "
        + "u.credentialsNonExpired = true, "
        + "u.enabled = true "
        + "WHERE u.id = :userId")
    void activateById(long userId);

    @Query("SELECT u.role FROM User u WHERE u.id = :userId")
    Role findRoleByUserId(long userId);
}
