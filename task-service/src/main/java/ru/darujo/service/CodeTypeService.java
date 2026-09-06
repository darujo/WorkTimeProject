package ru.darujo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.darujo.exceptions.ResourceNotFoundRunTime;
import ru.darujo.model.Code;
import ru.darujo.repository.CodeRepository;
import ru.darujo.specifications.Specifications;

@Service
public class CodeTypeService {

    private CodeRepository codeRepository;
    private CodeTypeAttrService codeTypeAttrService;

    public Code save(Code code) {
        return codeRepository.save(code);
    }

    public Page<Code> getCodes(Code parent, String code) {
        Specification<Code> sp = getSpec(parent, code, null);

        return Specifications.findAll(codeRepository, null, null, sp, "sort");
    }

    public String getAttrValue(Code parent, String code, String attr) {
        try {
            return codeTypeAttrService.findOne(findOne(parent, code), attr).getValue();
        } catch (ResourceNotFoundRunTime ignore) {
            return null;
        }
    }

    public Code findOne(Code parent, String code) {
        return codeRepository.findOne(getSpec(parent, code, parent == null)).orElseThrow(() -> new ResourceNotFoundRunTime("Не найден справочник"));
    }

    private Specification<Code> getSpec(Code parent, String code, Boolean isNotParent) {
        Specification<Code> sp = Specification.unrestricted();
        sp = Specifications.eq(sp, "parent", parent);
        sp = Specifications.isNull(sp, "parent", isNotParent);
        return Specifications.eq(sp, "code", code);
    }

    @Autowired
    public void setCodeRepository(CodeRepository codeRepository) {
        this.codeRepository = codeRepository;
    }

    @Autowired
    public void setCodeTypeAttrService(CodeTypeAttrService codeTypeAttrService) {
        this.codeTypeAttrService = codeTypeAttrService;
    }
}
