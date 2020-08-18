package br.com.its.cursomc.manager;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import br.com.its.cursomc.dao.ClienteDao;
import br.com.its.cursomc.dao.EnderecoDao;
import br.com.its.cursomc.domain.Cidade;
import br.com.its.cursomc.domain.Cliente;
import br.com.its.cursomc.domain.Endereco;
import br.com.its.cursomc.domain.enums.Perfil;
import br.com.its.cursomc.domain.enums.TipoCliente;
import br.com.its.cursomc.dto.ClienteDTO;
import br.com.its.cursomc.dto.NovoClienteDto;
import br.com.its.cursomc.manager.exception.AuthorizationException;
import br.com.its.cursomc.manager.exception.DataIntegrityException;
import br.com.its.cursomc.manager.exception.ObjectNotFoundException;
import br.com.its.cursomc.security.UserSS;

@Service
public class ClienteManager {

	@Value("${img.prefix.cliente.profile}")
	private String prefixoCliente;
	
	@Value("${img.profile.size}")
	private int tamanhoImagem;
	
	@Autowired
	private BCryptPasswordEncoder pe;
	
	@Autowired
	private ClienteDao dao;
	
	@Autowired
	private EnderecoDao enderecoDao;
	
	@Autowired
	private ImageManager imageManager;
	
	@Autowired
	private S3Manager s3Manager;
	
	public Cliente find(Integer id) {
		UserSS user = UserManager.authenticated();
		if ((user == null || !user.hasRole(Perfil.ADMIN)) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso negado");
		}
	    Optional<Cliente> obj = dao.findById(id);
	    return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}

	@Transactional
	public Cliente insert(Cliente element) {
		element.setId(null);
		element = dao.save(element);
		enderecoDao.saveAll(element.getEnderecos());
		return element;
	}

	public Cliente update(Cliente element) {
		Cliente novoCliente = find(element.getId());
		updateData(novoCliente, element);
		return dao.save(novoCliente);
	}
	
	private void updateData(Cliente novoCliente, Cliente element) {
		novoCliente.setNome(element.getNome());
		novoCliente.setEmail(element.getEmail());
	}
	
	public void delete(Integer id) {
		find(id);
		try {
			dao.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possivel excluir um cliente com pedidos relacionados");
		}
	}
	
	public List<Cliente> findAll() {
		return dao.findAll();
	}
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page,linesPerPage,Direction.valueOf(direction),orderBy);
		return dao.findAll(pageRequest);
	}
	
	public Cliente fromDTO(ClienteDTO objDto) {
		return new Cliente(objDto.getId(),objDto.getNome(),objDto.getEmail(),null,null,null);
	}

	public Cliente fromDTO(NovoClienteDto objDto) {
		Cliente cliente = new Cliente(null, objDto.getNome(), objDto.getEmail(), objDto.getCpfOuCnpj(), TipoCliente.toEnum(objDto.getTipo()), pe.encode(objDto.getSenha()));
		Cidade cidade = new Cidade(objDto.getCidadeId(), null, null);
		Endereco endereco = new Endereco(null, objDto.getLogradouro(), objDto.getNumero(), objDto.getComplemento(), objDto.getBairro(), objDto.getCep(), cliente, cidade);
		cliente.getEnderecos().add(endereco);
		cliente.getTelefones().add(objDto.getTelefone1());
		if (objDto.getTelefone2() != null) {
			cliente.getTelefones().add(objDto.getTelefone2());
		}
		if (objDto.getTelefone3() != null) {
			cliente.getTelefones().add(objDto.getTelefone3());
		}
		return cliente;
	}
	
	public URI uploadProfilePicture(MultipartFile multipartFile) {
		UserSS user = UserManager.authenticated();
		if (user == null) {
			throw new AuthorizationException("Acesso negado");
		}
		// carregando a imagem e ajustando o tipo e o tamanho
		BufferedImage jpgImage = imageManager.getJpgImageFromFile(multipartFile);
		jpgImage = imageManager.cropSquare(jpgImage);
		jpgImage = imageManager.resize(jpgImage, tamanhoImagem);
		// montando o nome do arquivo
		String fileName = prefixoCliente + user.getId() + ".jpg";
		// fazendo o upload do arquivo para o bucket do S3 da amazon
		return s3Manager.uploadFile(imageManager.getInputStream(jpgImage, "jpg"), fileName, "image");
	}
}
