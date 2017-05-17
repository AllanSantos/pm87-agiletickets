package br.com.caelum.agiletickets.domain.precos;

import java.math.BigDecimal;

import br.com.caelum.agiletickets.models.Sessao;
import br.com.caelum.agiletickets.models.TipoDeEspetaculo;

public class CalculadoraDePrecos {

	public static BigDecimal calcula(Sessao sessao, Integer quantidade) {
		BigDecimal preco;

		if (sessao.getEspetaculo().getTipo().equals(TipoDeEspetaculo.CINEMA)
				|| sessao.getEspetaculo().getTipo().equals(TipoDeEspetaculo.SHOW)) {
			
			if (calculaPercentualDeLotacao(sessao) <= 0.05) {
				preco = sessao.getPreco().add(sessao.getPreco().multiply(BigDecimal.valueOf(0.10)));
			} else {
				preco = sessao.getPreco();
			}

		} else if (sessao.getEspetaculo().getTipo().equals(TipoDeEspetaculo.BALLET)
				|| sessao.getEspetaculo().getTipo().equals(TipoDeEspetaculo.ORQUESTRA)) {
			if (calculaPercentualDeLotacao(sessao) <= 0.50) {
				preco = sessao.getPreco().add(sessao.getPreco().multiply(BigDecimal.valueOf(0.20)));
			} else {
				preco = sessao.getPreco();
			}

			if (sessao.getDuracaoEmMinutos() > 60) {
				preco = preco.add(sessao.getPreco().multiply(BigDecimal.valueOf(0.10)));
			}

		}

		else {
			preco = sessao.getPreco();
		}

		return preco.multiply(BigDecimal.valueOf(quantidade));
	}

	private static double calculaPercentualDeLotacao(Sessao sessao) {
		return getQtdIngressosDisponiveis(sessao) / sessao.getTotalIngressos().doubleValue();
	}

	private static int getQtdIngressosDisponiveis(Sessao sessao) {
		return sessao.getTotalIngressos() - sessao.getIngressosReservados();
	}

}