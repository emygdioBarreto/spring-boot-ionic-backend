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
import br.com.its.cursomc.dao.ProdutoDao;
import br.com.its.cursomc.domain.Categoria;
import br.com.its.cursomc.domain.Produto;
import br.com.its.cursomc.manager.exception.AuthorizationException;
import br.com.its.cursomc.manager.exception.DataIntegrityException;
import br.com.its.cursomc.manager.exception.ObjectNotFoundException;
import br.com.its.cursomc.security.UserSS;

@Service
public class ProdutoManager {

	@Value("${img.prefix.produto.profile}")
	private String prefixoProduto;
	
	@Value("${img.profile.size}")
	private int tamanhoImagem;
	
	@Autowired
	private CategoriaDao categoriaDao;
	
	@Autowired
	private ProdutoDao dao;
	
	@Autowired
	private ImageManager imageManager;
	
	@Autowired
	private S3Manager s3Manager;
	
	public Produto find(Integer id) {
	    Optional<Produto> obj = dao.findById(id);
	    return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + Produto.class.getName()));
	}

	public Produto insert(Produto element) {
		element.setId(null);
		return dao.save(element);
	}

	public Produto update(Produto element) {
		find(element.getId());
		return dao.save(element);
	}
	
	public void delete(Integer id) {
		find(id);
		try {
			dao.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possivel excluir um pedido com itens");
		}
	}
	
	public List<Produto> findAll() {
		return dao.findAll();
	}
	
	public Page<Produto> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page,linesPerPage,Direction.valueOf(direction),orderBy);
		return dao.findAll(pageRequest);
	}

	public Page<Produto> search(String nome, List<Integer> ids, Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page,linesPerPage,Direction.valueOf(direction),orderBy);
		List<Categoria> categorias = categoriaDao.findAllById(ids);
		return dao.findDistinctByNomeContainingAndCategoriasIn(nome, categorias, pageRequest);
	}

	
	public URI uploadProfilePicture(Integer id, MultipartFile multipartFile) {
		UserSS user = UserManager.authenticated();
		if (user == null) {
			throw new AuthorizationException("Acesso negado");
		}
		// verificando o Id recebido de produto
		if (id == null) {
			throw new ObjectNotFoundException("Favor informar o produto");
		}
		// checando o produto na base de dados
		Produto prod = this.find(id);
		if (prod == null) {
			throw new ObjectNotFoundException("Produto não cadastrado");
		}
		// carregando a imagem e ajustando o tipo e o tamanho
		BufferedImage jpgImage = imageManager.getJpgImageFromFile(multipartFile);
		jpgImage = imageManager.cropSquare(jpgImage);
		jpgImage = imageManager.resize(jpgImage, tamanhoImagem);
		// montando o nome do arquivo
		String fileName = prefixoProduto + prod.getId() + ".jpg";
		// fazendo o upload do arquivo para o bucket do S3 da amazon
		return s3Manager.uploadFile(imageManager.getInputStream(jpgImage, "jpg"), fileName, "image");
	}
}
