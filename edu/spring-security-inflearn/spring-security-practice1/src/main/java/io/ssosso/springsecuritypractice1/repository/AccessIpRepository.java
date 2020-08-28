package io.ssosso.springsecuritypractice1.repository;


import io.ssosso.springsecuritypractice1.domain.entity.AccessIp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessIpRepository extends JpaRepository<AccessIp, Long> {

    AccessIp findByIpAddress(String IpAddress);
}
