package sn.kiwi.apiwebsms.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import sn.kiwi.apiwebsms.models.UserInfo;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface UserDetailsRepository extends CrudRepository<UserInfo, String> {
	public UserInfo findByUserNameAndEnabled(String userName, short enabled);

	public List<UserInfo> findAllByEnabled(short enabled);

	public UserInfo findById(Integer id);
//
//	@Override
//	public UserInfo save(UserInfo userInfo);

	public void deleteById(Integer id);
}
