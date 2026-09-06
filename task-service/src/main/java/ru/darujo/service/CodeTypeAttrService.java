package ru.darujo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.darujo.exceptions.ResourceNotFoundRunTime;
import ru.darujo.model.Code;
import ru.darujo.model.CodeAttr;
import ru.darujo.repository.CodeAttrRepository;
import ru.darujo.specifications.Specifications;

@Service
public class CodeTypeAttrService {

    private CodeAttrRepository codeRepository;

    public CodeAttr save(CodeAttr code) {
        return codeRepository.save(code);
    }

    public Page<CodeAttr> getCodes(Code code, String attr) {
        Specification<CodeAttr> sp = getSpec(code, attr);

        return Specifications.findAll(codeRepository, null, null, sp, "sort");
    }

    public CodeAttr findOne(Code code, String attr) {
        return codeRepository.findOne(getSpec(code, attr)).orElseThrow(() -> new ResourceNotFoundRunTime("Не найден атрибут"));
    }

    private Specification<CodeAttr> getSpec(Code code, String attr) {
        Specification<CodeAttr> sp = Specification.unrestricted();
        sp = Specifications.eq(sp, "code", code);
        sp = Specifications.eq(sp, "attr", attr);
        return sp;
    }

    @Autowired
    public void setCodeRepository(CodeAttrRepository codeRepository) {
        this.codeRepository = codeRepository;
    }
}
