package io.ssosso.springsecuritypractice1.service.impl;

import io.ssosso.springsecuritypractice1.domain.dto.AccountDto;
import io.ssosso.springsecuritypractice1.domain.entity.Account;
import io.ssosso.springsecuritypractice1.domain.entity.Role;
import io.ssosso.springsecuritypractice1.repository.RoleRepository;
import io.ssosso.springsecuritypractice1.repository.UserRepository;
import io.ssosso.springsecuritypractice1.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Service("userService")
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Transactional
  @Override
  public void createUser(Account account){

    Role role = roleRepository.findByRoleName("ROLE_USER");
    Set<Role> roles = new HashSet<>();
    roles.add(role);
    account.setUserRoles(roles);
    userRepository.save(account);
  }

  @Transactional
  @Override
  public void modifyUser(AccountDto accountDto){

    ModelMapper modelMapper = new ModelMapper();
    Account account = modelMapper.map(accountDto, Account.class);

    if(accountDto.getRoles() != null){
      Set<Role> roles = new HashSet<>();
      accountDto.getRoles().forEach(role -> {
        Role r = roleRepository.findByRoleName(role);
        roles.add(r);
      });
      account.setUserRoles(roles);
    }
    account.setPassword(passwordEncoder.encode(accountDto.getPassword()));
    userRepository.save(account);

  }

  @Transactional
  public AccountDto getUser(Long id) {

    Account account = userRepository.findById(id).orElse(new Account());
    ModelMapper modelMapper = new ModelMapper();
    AccountDto accountDto = modelMapper.map(account, AccountDto.class);

    List<String> roles = account.getUserRoles()
      .stream()
      .map(role -> role.getRoleName())
      .collect(toList());

    accountDto.setRoles(roles);
    return accountDto;
  }

  @Transactional
  public List<Account> getUsers() {
    return userRepository.findAll();
  }

  @Override
  public void deleteUser(Long id) {
    userRepository.deleteById(id);
  }

  @Override
  @Secured("ROLE_USER")
  public void order() {
    System.out.println("order !!");
  }

}
