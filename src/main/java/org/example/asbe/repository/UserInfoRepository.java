package org.example.asbe.repository;

import org.example.asbe.model.dto.UserDTO;
import org.example.asbe.model.entity.Userinfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<Userinfo, Integer> {
    Optional<Userinfo> findByEmail(String email); // Use 'email' if that is the correct field for login

    Optional<Userinfo> findByUsername(String username); // Use 'username' if that is the correct field for login

    @Query("SELECT new org.example.asbe.model.dto.UserDTO(u.id, u.username, u.password) FROM Userinfo u where u.id = :id")
    Optional<UserDTO> getUserinfoById(@Param("id") Integer id); // Use 'username' if that is the correct field for login
}

