package it.manytomanyjpamaven.dao;

import java.util.List;

import it.manytomanyjpamaven.model.Ruolo;
import it.manytomanyjpamaven.model.StatoUtente;
import it.manytomanyjpamaven.model.Utente;

public interface UtenteDAO extends IBaseDAO<Utente> {
	
	public List<Utente> findAllByRuolo(Ruolo ruoloInput);
	public Utente findByIdFetchingRuoli(Long id);
	
	
	public List<Utente> cercaUtentiCreatiAGiugno() throws Exception;
	
	public int countQuantiUtentiSonoAdmin() throws Exception;
	
	public List<Utente> cercaUtentiConPwdMinoreDiOttoCaratteri() throws Exception;
	
	public boolean cercaSeAlmenoUnAdminDisabilitato() throws Exception;

}
