package model;

public enum TermostatoEnum {
	ZERO, UM, DOIS, TRES, QUATRO, CINCO, SEIS;

	public static TermostatoEnum indexOf(String value) {
		switch (value) {
			case "1":
				return UM;
			case "2":
				return DOIS;
			case "3":
				return TRES;
			case "4":
				return QUATRO;
			case "5":
				return CINCO;
			case "6":
				return SEIS;
			default:
				return ZERO;
		}

	}
	// n�o alterar a ordem dos n�meros pq pegando pelo .ordinal devolve o n�mero
	// certo ;)

}
