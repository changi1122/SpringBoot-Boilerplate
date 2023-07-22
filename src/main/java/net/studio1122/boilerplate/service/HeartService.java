package net.studio1122.boilerplate.service;

import jakarta.transaction.Transactional;
import net.studio1122.boilerplate.domain.Board;
import net.studio1122.boilerplate.domain.Heart;
import net.studio1122.boilerplate.domain.User;
import net.studio1122.boilerplate.repository.HeartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HeartService {

    private final HeartRepository heartRepository;

    @Autowired
    public HeartService(HeartRepository heartRepository) {
        this.heartRepository = heartRepository;
    }

    @Transactional
    public void create(Board board, User user) throws Exception {
        if (!heartRepository.existsByBoardIdAndByUserId(board.getId(), user.getId())) {
            Heart heart = new Heart();
            heart.setPost(board);
            heart.setUser(user);
            heartRepository.save(heart);
        } else {
            throw new Exception("already heart given");
        }
    }

    public Optional<Heart> findByBoardIdAndByUserId(Board board, User user) {
        return heartRepository.findByBoardIdAndByUserId(board.getId(), user.getId());
    }
}
