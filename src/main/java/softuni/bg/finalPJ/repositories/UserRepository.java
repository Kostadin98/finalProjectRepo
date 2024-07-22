package softuni.bg.finalPJ.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.bg.finalPJ.models.entities.UserEntity;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {


    Optional<UserEntity> findUserEntityByEmail(String email);
}
