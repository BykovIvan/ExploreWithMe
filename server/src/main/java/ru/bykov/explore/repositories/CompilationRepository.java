package ru.bykov.explore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.bykov.explore.model.Compilation;
import ru.bykov.explore.utils.StateOfEventAndReq;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    @Modifying
    @Query("update Compilation c set c.pinned = ?1 where c.id = ?2")
    void setPinnedFalseByCompId(Boolean pinned, Long compId);
}
