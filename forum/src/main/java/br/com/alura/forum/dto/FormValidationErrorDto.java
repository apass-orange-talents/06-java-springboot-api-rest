package br.com.alura.forum.dto;

public class FormValidationErrorDto {
    public String campo;
    public String erro;

    public FormValidationErrorDto(String campo, String erro)
    {
        this.campo = campo;
        this.erro = erro;
    }

    public String getCampo() {
        return campo;
    }

    public String getErro() {
        return erro;
    }
}
