package vinci.ma.inventory.dao.repositories;


import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vinci.ma.inventory.dao.entities.Notification;



@Repository
public interface NotificationRepo extends JpaRepository<Notification, Long> {


    void deleteByUserId(Long id);
    // Custom query methods (if any)


    @Transactional
    @Modifying
    @Query("DELETE FROM Notification n WHERE n.admin.id = :adminId")
    void deleteByAdminId(Long adminId);
    Page<Notification> findAllByAdminId(Long adminId, Pageable pageable);
}