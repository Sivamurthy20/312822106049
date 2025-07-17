package com._9.repository;

import com._9.entity.Click;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClickRepository extends JpaRepository<Click, Long> {
    java.util.List<Click> findByShortcode(String shortcode);
}
