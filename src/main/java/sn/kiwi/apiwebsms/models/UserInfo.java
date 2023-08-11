package sn.kiwi.apiwebsms.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "api_websms_users")
public class UserInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", length = 25)
	private Integer id;

	@Column(name = "username", length = 50)
	private String userName;

	@Column(name = "password", length = 800)
	private String password;

	@Column(name = "role", length = 50)
	private String role;

	@Column(name = "enabled")
	private short enabled;

	@Column(name = "user_id")
	private Integer user_id;

	@Column(name = "partner_id")
	private Integer partner_id;

	@Column(name = "customer_id")
	private Integer customer_id;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public void setPartner_id(Integer partner_id) {
		this.partner_id = partner_id;
	}

	public void setCustomer_id(Integer customer_id) {
		this.customer_id = customer_id;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public short getEnabled() {
		return enabled;
	}

	public void setEnabled(short enabled) {
		this.enabled = enabled;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public Integer getPartner_id() {
		return partner_id;
	}

	public Integer getCustomer_id() {
		return customer_id;
	}

	@Override
	public String toString() {
		return String.format("UserInfo [id=%s, userName=%s, password=%s, role=%s, enabled=%s]", id, userName, password,
				role, enabled);
	}

}
