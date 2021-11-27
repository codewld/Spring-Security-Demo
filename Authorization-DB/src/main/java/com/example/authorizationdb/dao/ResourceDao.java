package com.example.authorizationdb.dao;

import com.example.authorizationdb.bean.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceDao extends JpaRepository<Resource, Long> {

}
