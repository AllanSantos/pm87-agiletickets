package br.com.caelum.agiletickets.domain.precos;

import java.math.BigDecimal;

import br.com.caelum.agiletickets.models.Sessao;
import br.com.caelum.agiletickets.models.TipoDeEspetaculo;

public class CalculadoraDePrecos {
	
	private static double taxa = 0.0;
	private static double multiplicador = 0.0;

	public static BigDecimal calcula(Sessao sessao, Integer quantidade) {
		BigDecimal preco;

		preco = calculaPrecoPorLotacao(sessao);

		return preco.multiply(BigDecimal.valueOf(quantidade));
	}
	
	private static BigDecimal calculaPrecoPorLotacao(Sessao sessao) {
		BigDecimal preco;
		
		if (getTipoDeEspetaculo(sessao).equals(TipoDeEspetaculo.CINEMA)
				|| getTipoDeEspetaculo(sessao).equals(TipoDeEspetaculo.SHOW)) {
			
			taxa = 0.05;
			multiplicador = 0.10;
			
			preco = getCalculaPrecoTaxado(sessao);

		} else if (getTipoDeEspetaculo(sessao).equals(TipoDeEspetaculo.BALLET)
				|| getTipoDeEspetaculo(sessao).equals(TipoDeEspetaculo.ORQUESTRA)) {
			
			taxa = 0.50;
			multiplicador = 0.20;
			
			preco = getCalculaPrecoTaxado(sessao);

			preco = getEspetaculoMaiorQueUmaHora(sessao, preco);

		}

		else {
			preco = sessao.getPreco();
		}

		return preco;
	}

	private static BigDecimal getEspetaculoMaiorQueUmaHora(Sessao sessao, BigDecimal preco) {
		if (sessao.getDuracaoEmMinutos() > 60) {
			preco = preco.add(sessao.getPreco().multiply(BigDecimal.valueOf(0.10)));
		}
		return preco;
	}

	private static BigDecimal getCalculaPrecoTaxado(Sessao sessao) {
		BigDecimal preco;
		if (calculaPercentualDeLotacao(sessao) <= taxa) {
			preco = sessao.getPreco().add(sessao.getPreco().multiply(BigDecimal.valueOf(multiplicador)));
		} else {
			preco = sessao.getPreco();
		}
		
		
		
		return preco;
	}
	
	private static TipoDeEspetaculo getTipoDeEspetaculo(Sessao sessao) {
		return sessao.getEspetaculo().getTipo();
	}

	private static double calculaPercentualDeLotacao(Sessao sessao) {
		return getQtdIngressosDisponiveis(sessao) / sessao.getTotalIngressos().doubleValue();
	}

	private static int getQtdIngressosDisponiveis(Sessao sessao) {
		return sessao.getTotalIngressos() - sessao.getIngressosReservados();
	}

}