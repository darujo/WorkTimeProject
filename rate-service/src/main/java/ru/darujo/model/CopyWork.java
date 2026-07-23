package ru.darujo.model;

import org.springframework.data.repository.CrudRepository;
import ru.darujo.exceptions.ResourceNotFoundRunTime;

public abstract class CopyWork implements Cloneable {

    public abstract void setId(Long id);

    public abstract void setWorkId(Long workIdTarget);

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public static <T extends CopyWork> void copy(Long workIdTarget, Boolean deleteOld, T work, CrudRepository<T, Long> repo) {
        T workSave;
        if (!deleteOld) {
            try {

                workSave = (T) work.clone();
            } catch (CloneNotSupportedException w) {
                throw new ResourceNotFoundRunTime(w.getMessage());
            }
            workSave.setId(null);
        } else {
            workSave = work;
        }
        workSave.setWorkId(workIdTarget);
        repo.save(workSave);
    }
}
