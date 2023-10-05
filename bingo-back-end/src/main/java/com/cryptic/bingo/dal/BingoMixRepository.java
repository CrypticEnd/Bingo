package com.cryptic.bingo.dal;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cryptic.bingo.model.BingoMix;
import com.cryptic.bingo.model.User;

@Repository
public interface BingoMixRepository extends JpaRepository<BingoMix, Integer> {

	List<BingoMix> getByOwnerOrderByName(User user);
	List<BingoMix> getByOwnerIsNotOrderByName(User user);
	List<BingoMix> getByOwnerIsNullOrderByName();

}
