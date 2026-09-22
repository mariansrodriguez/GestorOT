package com.mycompany.gestorot.modelo;

 
    public class ItemCombo {
    private int id;
    private String texto;

    public ItemCombo(int id, String texto) {
        this.id = id;
        this.texto = texto;
    }

    public int getId() {
        return id;
    }

    // Este método es la clave: JComboBox llama a toString() automáticamente
    // para decidir qué texto mostrar en la lista. Así, aunque el objeto
    // guarda el id por dentro, en pantalla el usuario solo ve el nombre.
    @Override
    public String toString() {
        return texto;
    }
}

