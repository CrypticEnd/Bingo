package com.cryptic.bingo.dal;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cryptic.bingo.model.Game;
import com.cryptic.bingo.model.User;

@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {

	boolean existsByJoinCode(int gamecode);

	Optional<Game> findByJoinCode(int gamecode);

	List<Game> getAllByOwnerOrderByName(User owner);
}
