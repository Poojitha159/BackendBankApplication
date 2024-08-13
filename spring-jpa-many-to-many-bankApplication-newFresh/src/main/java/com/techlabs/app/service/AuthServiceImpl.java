package com.techlabs.app.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.techlabs.app.dto.LoginDto;
import com.techlabs.app.dto.RegisterDto;
import com.techlabs.app.entity.Customer;
import com.techlabs.app.entity.FileItem;
import com.techlabs.app.entity.Role;
import com.techlabs.app.entity.User;
import com.techlabs.app.exception.APIException;
import com.techlabs.app.exception.UserException;
import com.techlabs.app.repository.CustomerRepository;
import com.techlabs.app.repository.RoleRepository;
import com.techlabs.app.repository.UserRepository;
import com.techlabs.app.security.JwtTokenProvider;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class AuthServiceImpl implements AuthService {
	
	@Autowired
    private FileService fileService;


	private AuthenticationManager authenticationManager;
	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private PasswordEncoder passwordEncoder;
	private JwtTokenProvider jwtTokenProvider;
	private CustomerRepository customerRepository;

	/*public AuthServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository,
			RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider,
			CustomerRepository customerRepository) {
		super();
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtTokenProvider = jwtTokenProvider;
		this.customerRepository = customerRepository;
	}
	*/
	public AuthServiceImpl(FileService fileService, AuthenticationManager authenticationManager,
			UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder,
			JwtTokenProvider jwtTokenProvider, CustomerRepository customerRepository) {
		super();
		this.fileService = fileService;
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtTokenProvider = jwtTokenProvider;
		this.customerRepository = customerRepository;
	}

	@Override
	public String login(LoginDto loginDto) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(), loginDto.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String token = jwtTokenProvider.generateToken(authentication);

		return token;
	}

	@Override
	public String register(RegisterDto registerDto, MultipartFile file1, MultipartFile file2) throws IllegalStateException, IOException {

		
		
		

		if (userRepository.existsByUsername(registerDto.getUsername())) {
			throw new APIException(HttpStatus.BAD_REQUEST, "Username is already exists!.");
		}

		if (userRepository.existsByEmail(registerDto.getEmail())) {
			throw new APIException(HttpStatus.BAD_REQUEST, "Email is already exists!.");
		}
		User user = new User();
		user.setName(registerDto.getFirstName() + registerDto.getLastName());
		user.setUsername(registerDto.getUsername());
		user.setEmail(registerDto.getEmail());
		user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

		
		
		Set<Role> roles = new HashSet<>();
		
		Optional<Role> userRoleOpt = roleRepository.findByName(registerDto.getRole());
        if (userRoleOpt.isEmpty()) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Role not found!");
        }
		Role userRole = roleRepository.findByName(registerDto.getRole()).get();
		roles.add(userRole);
		user.setRoles(roles);

/*		if (registerDto.getRole().equals("ROLE_USER")) {
			if(file1!=null && !file1.isEmpty()) {
				uploadFile(file1);
			}
			if(file2!=null && !file2.isEmpty()) {
				uploadFile(file2);
			}
			
			

			Customer customer = new Customer();
			customer.setFirstName(registerDto.getFirstName());
			customer.setLastName(registerDto.getLastName());
			customer.setTotalBalance(1000);
			customer.setUser(user);
			user.setCustomer(customer);
		}
*/
		
		if (registerDto.getRole().equals("ROLE_USER")) {
            if (file1 == null || file1.isEmpty() || file2 == null || file2.isEmpty()) {
                throw new APIException(HttpStatus.BAD_REQUEST, "Files are required for role USER!");
            }
            

            Customer customer = new Customer();
            customer.setFirstName(registerDto.getFirstName());
            customer.setLastName(registerDto.getLastName());
            customer.setTotalBalance(1000);
            customer.setUser(user);
            user.setCustomer(customer);
        } else if (registerDto.getRole().equals("ROLE_ADMIN")) {
            if (file1 != null && !file1.isEmpty() || file2 != null && !file2.isEmpty()) {
                throw new APIException(HttpStatus.BAD_REQUEST, "No need of file for Role ADMIN!");
            }
        } else {
            throw new APIException(HttpStatus.BAD_REQUEST, "Unsupported role!");
        }

        
       // return new ResponseEntity<>(responseItem, HttpStatus.OK);
    
        
		User savedUser = userRepository.save(user);
		uploadFile(file1,savedUser.getId());
        uploadFile(file2,savedUser.getId());
		// customer.setUser(user);
		// customerRepository.save(customer);

		return "User registered successfully!.";
	}

	private void uploadFile(MultipartFile file, Long userId) throws IllegalStateException, IOException {

		String directory_path = "src/main/java/com/techlabs/app/attachments/";
		  
		  if (file.isEmpty()) {
		            throw new UserException("Please select a file to upload.");
		        }

		        try {
		         directory_path = directory_path + userId + "/";
		            File directory = new File(directory_path);
		            if (!directory.exists()) {
		             directory.mkdirs();
		            }

		            Path path = Paths.get(directory_path + file.getOriginalFilename());
		            if (Files.exists(path)) {
		             throw new UserException("File already exists: " + file.getOriginalFilename());
		            }
		            Files.write(path, file.getBytes());
		        } 
		        catch (IOException e) {
		         
		            throw new UserException("Could not upload the file: Error Occurred");
		        }


	}
}
