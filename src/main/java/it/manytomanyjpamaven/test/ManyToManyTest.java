package it.manytomanyjpamaven.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import it.manytomanyjpamaven.dao.EntityManagerUtil;
import it.manytomanyjpamaven.model.Ruolo;
import it.manytomanyjpamaven.model.StatoUtente;
import it.manytomanyjpamaven.model.Utente;
import it.manytomanyjpamaven.service.MyServiceFactory;
import it.manytomanyjpamaven.service.RuoloService;
import it.manytomanyjpamaven.service.UtenteService;

public class ManyToManyTest {

	public static void main(String[] args) {
		UtenteService utenteServiceInstance = MyServiceFactory.getUtenteServiceInstance();
		RuoloService ruoloServiceInstance = MyServiceFactory.getRuoloServiceInstance();

		// ora passo alle operazioni CRUD
		try {

			// inizializzo i ruoli sul db
			// initRuoli(ruoloServiceInstance);
			/*
			System.out.println("In tabella Utente ci sono " + utenteServiceInstance.listAll().size() + " elementi.");
			*/
			// testInserisciNuovoUtente(utenteServiceInstance);
			// System.out.println("In tabella Utente ci sono " + utenteServiceInstance.listAll().size() + " elementi.");
			/*
			testCollegaUtenteARuoloEsistente(ruoloServiceInstance, utenteServiceInstance);
			System.out.println("In tabella Utente ci sono " + utenteServiceInstance.listAll().size() + " elementi.");
			*/  /*
			testModificaStatoUtente(utenteServiceInstance);
			System.out.println("In tabella Utente ci sono " + utenteServiceInstance.listAll().size() + " elementi.");
			*/
			/*testRimuoviRuoloDaUtente(ruoloServiceInstance, utenteServiceInstance);
			System.out.println("In tabella Utente ci sono " + utenteServiceInstance.listAll().size() + " elementi.");
			*/
			
			// testListRuolo(ruoloServiceInstance);
			
			// testUpdateRuolo(ruoloServiceInstance);
			
			// testDeleteRuolo(ruoloServiceInstance);
			
			// testDeleteUtente(utenteServiceInstance);
			
			//testCercaUtentiCreatiAGiugno(utenteServiceInstance);
			
			//testCountQuantiUtentiSonoAdmin(utenteServiceInstance);
			
			//testCercaUtentiConPwdMinoreDiOttoCaratteri(utenteServiceInstance);
			
			//testCercaDescrizioneQuantiRuoliConUtentiAssociati(ruoloServiceInstance);
			
			testCercaSeAlmenoUnAdminDisabilitato(utenteServiceInstance);
			
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			// questa è necessaria per chiudere tutte le connessioni quindi rilasciare il
			// main
			EntityManagerUtil.shutdown();
		}

	}

	private static void initRuoli(RuoloService ruoloServiceInstance) throws Exception {
		if (ruoloServiceInstance.cercaPerDescrizioneECodice("Administrator", "ROLE_ADMIN") == null) {
			ruoloServiceInstance.inserisciNuovo(new Ruolo("Administrator", "ROLE_ADMIN"));
		}

		if (ruoloServiceInstance.cercaPerDescrizioneECodice("Classic User", "ROLE_CLASSIC_USER") == null) {
			ruoloServiceInstance.inserisciNuovo(new Ruolo("Classic User", "ROLE_CLASSIC_USER"));
		}
	}

	private static void testInserisciNuovoUtente(UtenteService utenteServiceInstance) throws Exception {
		System.out.println(".......testInserisciNuovoUtente inizio.............");

		Date dataCreazione = new SimpleDateFormat("dd-MM-yyyy").parse("03-06-2021");
		
		Utente utenteNuovo = new Utente("gio.pesci", "giooooo", "giovanni", "pesci", dataCreazione);
		utenteServiceInstance.inserisciNuovo(utenteNuovo);
		if (utenteNuovo.getId() == null)
			throw new RuntimeException("testInserisciNuovoUtente fallito ");

		System.out.println(".......testInserisciNuovoUtente fine: PASSED.............");
	}

	private static void testCollegaUtenteARuoloEsistente(RuoloService ruoloServiceInstance,
			UtenteService utenteServiceInstance) throws Exception {
		System.out.println(".......testCollegaUtenteARuoloEsistente inizio.............");

		Ruolo ruoloEsistenteSuDb = ruoloServiceInstance.cercaPerDescrizioneECodice("Administrator", "ROLE_ADMIN");
		if (ruoloEsistenteSuDb == null)
			throw new RuntimeException("testCollegaUtenteARuoloEsistente fallito: ruolo inesistente ");

		// mi creo un utente inserendolo direttamente su db
		Utente utenteNuovo = new Utente("mario.bianchi", "JJJ", "mario", "bianchi", new Date());
		utenteServiceInstance.inserisciNuovo(utenteNuovo);
		if (utenteNuovo.getId() == null)
			throw new RuntimeException("testInserisciNuovoUtente fallito: utente non inserito ");

		utenteServiceInstance.aggiungiRuolo(utenteNuovo, ruoloEsistenteSuDb);
		// per fare il test ricarico interamente l'oggetto e la relazione
		Utente utenteReloaded = utenteServiceInstance.caricaUtenteSingoloConRuoli(utenteNuovo.getId());
		if (utenteReloaded.getRuoli().size() != 1)
			throw new RuntimeException("testInserisciNuovoUtente fallito: ruoli non aggiunti ");

		System.out.println(".......testCollegaUtenteARuoloEsistente fine: PASSED.............");
	}

	private static void testModificaStatoUtente(UtenteService utenteServiceInstance) throws Exception {
		System.out.println(".......testModificaStatoUtente inizio.............");

		// mi creo un utente inserendolo direttamente su db
		Utente utenteNuovo = new Utente("mario1.bianchi1", "JJJ", "mario1", "bianchi1", new Date());
		utenteServiceInstance.inserisciNuovo(utenteNuovo);
		if (utenteNuovo.getId() == null)
			throw new RuntimeException("testModificaStatoUtente fallito: utente non inserito ");

		// proviamo a passarlo nello stato ATTIVO ma salviamoci il vecchio stato
		StatoUtente vecchioStato = utenteNuovo.getStato();
		utenteNuovo.setStato(StatoUtente.ATTIVO);
		utenteServiceInstance.aggiorna(utenteNuovo);

		if (utenteNuovo.getStato().equals(vecchioStato))
			throw new RuntimeException("testModificaStatoUtente fallito: modifica non avvenuta correttamente ");

		System.out.println(".......testModificaStatoUtente fine: PASSED.............");
	}

	private static void testRimuoviRuoloDaUtente(RuoloService ruoloServiceInstance, UtenteService utenteServiceInstance)
			throws Exception {
		System.out.println(".......testRimuoviRuoloDaUtente inizio.............");

		// carico un ruolo e lo associo ad un nuovo utente
		Ruolo ruoloEsistenteSuDb = ruoloServiceInstance.cercaPerDescrizioneECodice("Administrator", "ROLE_ADMIN");
		if (ruoloEsistenteSuDb == null)
			throw new RuntimeException("testRimuoviRuoloDaUtente fallito: ruolo inesistente ");

		// mi creo un utente inserendolo direttamente su db
		Utente utenteNuovo = new Utente("aldo.manuzzi", "pwd@2", "aldo", "manuzzi", new Date());
		utenteServiceInstance.inserisciNuovo(utenteNuovo);
		if (utenteNuovo.getId() == null)
			throw new RuntimeException("testRimuoviRuoloDaUtente fallito: utente non inserito ");
		utenteServiceInstance.aggiungiRuolo(utenteNuovo, ruoloEsistenteSuDb);

		// ora ricarico il record e provo a disassociare il ruolo
		Utente utenteReloaded = utenteServiceInstance.caricaUtenteSingoloConRuoli(utenteNuovo.getId());
		boolean confermoRuoloPresente = false;
		for (Ruolo ruoloItem : utenteReloaded.getRuoli()) {
			if (ruoloItem.getCodice().equals(ruoloEsistenteSuDb.getCodice())) {
				confermoRuoloPresente = true;
				break;
			}
		}

		if (!confermoRuoloPresente)
			throw new RuntimeException("testRimuoviRuoloDaUtente fallito: utente e ruolo non associati ");

		// ora provo la rimozione vera e propria ma poi forzo il caricamento per fare un confronto 'pulito'
		utenteServiceInstance.rimuoviRuoloDaUtente(utenteReloaded.getId(), ruoloEsistenteSuDb.getId());
		utenteReloaded = utenteServiceInstance.caricaUtenteSingoloConRuoli(utenteNuovo.getId());
		if (!utenteReloaded.getRuoli().isEmpty())
			throw new RuntimeException("testRimuoviRuoloDaUtente fallito: ruolo ancora associato ");

		System.out.println(".......testRimuoviRuoloDaUtente fine: PASSED.............");
	}

	private static void testListRuolo(RuoloService ruoloServiceInstance) throws Exception{
		List<Ruolo> list = ruoloServiceInstance.listAll();
		
		System.out.println(list.size());
	}
	
	private static void testUpdateRuolo(RuoloService ruoloServiceInstance) throws Exception{
		
		Ruolo daModificare = new Ruolo();
		
		daModificare = ruoloServiceInstance.listAll().get(1);
		
		daModificare.setDescrizione("Classic User Modificato");
		daModificare.setCodice("ROLE_CLASSIC_USER");
		
		ruoloServiceInstance.aggiorna(daModificare);
		
		System.out.println(daModificare.getDescrizione());	
	}
	
	
	private static void testDeleteRuolo(RuoloService ruoloServiceInstance) throws Exception{
		
		Long idDaEliminare = ruoloServiceInstance.listAll().get(2).getId();
		
		ruoloServiceInstance.rimuovi(idDaEliminare);
		
		System.out.println(ruoloServiceInstance.listAll().size());
		
	}
	
	private static void testDeleteUtente(UtenteService utenteServiceInstance) throws Exception{
		
		long idDaEliminare = utenteServiceInstance.listAll().get(2).getId();
		
		utenteServiceInstance.rimuovi(idDaEliminare);
		
		System.out.println(utenteServiceInstance.listAll().size());	
	}
	
	
	private static void testCercaUtentiCreatiAGiugno(UtenteService utenteServiceInstance) throws Exception{
		if(utenteServiceInstance.listAll().isEmpty())
			throw new Exception("Lista utenti vuota");
		
		System.out.println(utenteServiceInstance.cercaUtentiCreatiAGiugno());
		
		
			
	}
	
	private static void testCountQuantiUtentiSonoAdmin(UtenteService utenteServiceInstance) throws Exception{
		if(utenteServiceInstance.listAll().isEmpty())
			throw new Exception("Lista utenti vuota");
		
		System.out.println(utenteServiceInstance.countQuantiUtentiSonoAdmin());
	}
	
	
	private static void testCercaUtentiConPwdMinoreDiOttoCaratteri(UtenteService utenteServiceInstance) throws Exception{
		List<Utente> result = utenteServiceInstance.cercaUtentiConPwdMinoreDiOttoCaratteri();
		
		System.out.println(result.size());
	}
	
	private static void testCercaDescrizioneQuantiRuoliConUtentiAssociati(RuoloService ruoloServiceInstance) throws Exception{
		
		List<String> result = ruoloServiceInstance.cercaDescrizioneQuantiRuoliConUtentiAssociati();
		
		System.out.println(result.size());
		
	}
	
	
	private static void testCercaSeAlmenoUnAdminDisabilitato(UtenteService utenteServiceInstance) throws Exception{
		
		if(utenteServiceInstance.cercaSeAlmenoUnAdminDisabilitato())
			throw new Exception("Nessun Admin disabilitato trovato");
		else 
			System.out.println("Admin disabilitato trovato");
	}
	
}
