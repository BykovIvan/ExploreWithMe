package ru.bykov.explore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.bykov.explore.model.Compilation;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    @Modifying
    @Query("update Compilation c set c.pinned = :pinner where c.id = :comp_id")
    void setPinnedByCompId(@Param("pinner") Boolean pinned,
                           @Param("comp_id") Long compId);
}
