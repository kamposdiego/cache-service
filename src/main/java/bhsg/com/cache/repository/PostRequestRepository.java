package bhsg.com.cache.repository;

import bhsg.com.cache.entity.PostRequestRedisHash;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRequestRepository extends CrudRepository<PostRequestRedisHash, String> {

}
