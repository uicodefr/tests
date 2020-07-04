package test.uicode.mongoperf.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import test.uicode.mongoperf.entity.BaseEntity;

@Repository
public interface BaseDao extends MongoRepository<BaseEntity, String> {

    @Query(fields = "{names: 1, date: 1}", value = "{'names.value': {$regex: ?0}, 'names.lang': 'en'}")
    public Page<BaseEntity> search(String name, Pageable pageable);

    @Query(fields = "{names: 1, date: 1}", value="{}")
    public List<BaseEntity> findAllLight();

}
