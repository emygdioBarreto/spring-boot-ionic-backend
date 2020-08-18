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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.its.cursomc.dao.CategoriaDao;
import br.com.its.cursomc.domain.Categoria;
import br.com.its.cursomc.dto.CategoriaDTO;
import br.com.its.cursomc.manager.exception.AuthorizationException;
import br.com.its.cursomc.manager.exception.DataIntegrityException;
import br.com.its.cursomc.manager.exception.ObjectNotFoundException;
import br.com.its.cursomc.security.UserSS;

@Service
public class CategoriaManager {
	
	@Value("${img.prefix.categoria.profile}")
	private String prefixoCategoria;
	
	@Value("${img.profile.size}")
	private int tamanhoImagem;
	
	@Autowired
	private CategoriaDao dao;
	
	@Autowired
	private ImageManager imageManager;
	
	@Autowired
	private S3Manager s3Manager;
	
	public Categoria find(Integer id) {
	    Optional<Categoria> obj = dao.findById(id);
	    return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));
	}
	
	public Categoria insert(Categoria element) {
		element.setId(null);
		return dao.save(element);
	}

	public Categoria update(Categoria element) {
		Categoria novaCategoria = find(element.getId());
		updateData(novaCategoria, element);
		return dao.save(element);
	}
	
	private void updateData(Categoria novaCategoria, Categoria element) {
		novaCategoria.setNome(element.getNome());
	}
	
	public void delete(Integer id) {
		find(id);
		try {
			dao.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possivel excluir uma categoria com produtos");
		}
	}
	
	public List<Categoria> findAll() {
		return dao.findAll();
	}
	
	public Page<Categoria> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page,linesPerPage,Direction.valueOf(direction),orderBy);
		return dao.findAll(pageRequest);
	}
	
	public Categoria fromDTO(CategoriaDTO objDto) {
		return new Categoria(objDto.getId(),objDto.getNome());
	}
	
	public URI uploadProfilePicture(Integer id, MultipartFile multipartFile) {
		UserSS user = UserManager.authenticated();
		if (user == null) {
			throw new AuthorizationException("Acesso negado");
		}
		// verificando o Id recebido de categoria
		if (id == null) {
			throw new ObjectNotFoundException("Favor informar a categoria");
		}
		// checando a categoria na base de dados
		Categoria cat = this.find(id);
		if (cat == null) {
			throw new ObjectNotFoundException("Categoria não cadastrada");
		}
		// carregando a imagem e ajustando o tipo e o tamanho
		BufferedImage jpgImage = imageManager.getJpgImageFromFile(multipartFile);
		jpgImage = imageManager.cropSquare(jpgImage);
		jpgImage = imageManager.resize(jpgImage, tamanhoImagem);
		// montando o nome do arquivo
		String fileName = prefixoCategoria + cat.getId() + ".jpg";
		// fazendo o upload do arquivo para o bucket do S3 da amazon
		return s3Manager.uploadFile(imageManager.getInputStream(jpgImage, "jpg"), fileName, "image");
	}
}