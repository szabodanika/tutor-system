package service;

import java.util.List;

public interface CustomService<T, ID>{
	
	public List<T> findAll();
	
	public T findById(ID id);
	
	public void deleteById(ID id);
	
	public void save(T item);
}
