package org.itsci.home4paws.services;

import org.itsci.home4paws.DTO.PostAnimalRequest;
import org.itsci.home4paws.DTO.PostAnimalResponse;
import org.itsci.home4paws.model.PostAnimal;

import java.util.List;

public interface PostAnimalService {
    String doAddPost(PostAnimal p, String username);
    List<PostAnimal> getListPostByUser(String username);
    PostAnimal getPostById(String animalId);
    String editPost(String animalId, PostAnimalRequest updatedRequest);
    void deletePostById(String animalId);
    List<PostAnimalResponse> getListPost();
    List<PostAnimalResponse> searchPosts(String keyword);
}
