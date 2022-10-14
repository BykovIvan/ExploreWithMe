package ru.bykov.explore.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.bykov.explore.model.Compilation;

import java.util.Optional;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    @Query("select c from Compilation c " +
            "WHERE c.pinned is null or c.pinned = (:pinned)")
    Page<Compilation> findAllByPinned(@Param("pinned") Boolean pinned, Pageable pageable);
    @Query(value = "SELECT * FROM compilations WHERE id = ?1", nativeQuery = true)
    Optional<Compilation> findById(Long compId);
    @Modifying
    @Query("update Compilation c set c.pinned = :pinned where c.id = :comp_id")
    void setPinnedByCompId(@Param("pinned") Boolean pinned,
                           @Param("comp_id") Long compId);
}
