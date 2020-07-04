package test.uicode.mongoperf.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import test.uicode.mongoperf.ResultDto;
import test.uicode.mongoperf.dao.BaseDao;
import test.uicode.mongoperf.entity.BaseEntity;
import test.uicode.mongoperf.entity.NameDocument;
import test.uicode.mongoperf.entity.SubDocument;

@Service
public class OneService {

    private static final Integer ENTITY_NAME_LENGTH = 5;
    private static final List<String> ENTITY_NAME_LANGS = Arrays.asList("en", "fr", "es", "it", "pt");
    private static final Integer OTHERS_LENGTH = 1000;
    private static final Integer PAGE_SIZE = 20;

    private Random random = new Random();

    @Autowired
    private BaseDao baseDao;

    public void initEntities(Integer entityNumber, Integer entitySize) {
        for (int i = 0; i < entityNumber; i++) {
            BaseEntity entity = new BaseEntity();
            entity.setDate(new Date());
            entity.setName(generateString(ENTITY_NAME_LENGTH));

            entity.setNames(new ArrayList<>());
            for (String lang : ENTITY_NAME_LANGS) {
                NameDocument nameDocument = new NameDocument();
                nameDocument.setLang(lang);
                nameDocument.setValue(generateString(ENTITY_NAME_LENGTH));
                entity.getNames().add(nameDocument);
            }

            entity.setDocumentList(new ArrayList<>());
            for (int j = 0; j < entitySize; j++) {
                SubDocument subDocument = new SubDocument();
                subDocument.setDate(new Date());
                subDocument.setName(generateString(ENTITY_NAME_LENGTH * 2));
                subDocument.setOthers(new ArrayList<>());

                for (int k = 0; k < OTHERS_LENGTH; k++) {
                    subDocument.getOthers().add(subDocument.getName());
                }
                entity.getDocumentList().add(subDocument);
            }

            baseDao.save(entity);
        }
    }

    public ResultDto searchEntities(String nameFilter) {
        Pageable pageable = PageRequest.of(0, PAGE_SIZE, Sort.by("name"));
        Page<BaseEntity> searchResult = baseDao.search(nameFilter, pageable);

        ResultDto resultDto = new ResultDto();
        resultDto.setData(searchResult.getContent());
        resultDto.setTotalElements(searchResult.getTotalElements());
        resultDto.setTotalPages(searchResult.getTotalPages());

        return resultDto;
    }

    private String generateString(Integer length) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'

        return random.ints(leftLimit, rightLimit + 1)
            .limit(length)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();
    }

    public List<BaseEntity> getAllEntities() {
        return baseDao.findAllLight();
    }

}
