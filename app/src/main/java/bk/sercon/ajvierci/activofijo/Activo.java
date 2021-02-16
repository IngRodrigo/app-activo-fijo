package bk.sercon.ajvierci.activofijo;

public class Activo {

    private String NUMCHAPA;
    private String NUMACTIVO;
    private String DESCRIPCION;
    private String ESTADO;
    private String DESCESTADO;
    private String UBICACION;
    private String DESCUBICACION;
    private String CAT7;
    private String NUMCOMPA;
    private String USUASIGNADO;
    private String COSTO;
    private String DEPACU;


    //INICIO DEVROD
    private String userRegistro, fechaPick, nombreAuditoria, sucursal, pick, registrado;

    public String getUserRegistro() {
        return userRegistro;
    }

    public void setUserRegistro(String userRegistro) {
        this.userRegistro = userRegistro;
    }

    public String getFechaPick() {
        return fechaPick;
    }

    public void setFechaPick(String fechaPick) {
        this.fechaPick = fechaPick;
    }

    public String getNombreAuditoria() {
        return nombreAuditoria;
    }

    public void setNombreAuditoria(String nombreAuditoria) {
        this.nombreAuditoria = nombreAuditoria;
    }

    public String getSucursal() {
        return sucursal;
    }

    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }

    public String getPick() {
        return pick;
    }

    public void setPick(String pick) {
        this.pick = pick;
    }

    public String getRegistrado() {
        return registrado;
    }

    public void setRegistrado(String registrado) {
        this.registrado = registrado;
    }

    public void setNUMCHAPA(String NUMCHAPA) {
        this.NUMCHAPA = NUMCHAPA;
    }

    public void setNUMACTIVO(String NUMACTIVO) {
        this.NUMACTIVO = NUMACTIVO;
    }

    public void setDESCRIPCION(String DESCRIPCION) {
        this.DESCRIPCION = DESCRIPCION;
    }

    public void setESTADO(String ESTADO) {
        this.ESTADO = ESTADO;
    }

    public void setDESCESTADO(String DESCESTADO) {
        this.DESCESTADO = DESCESTADO;
    }

    public void setUBICACION(String UBICACION) {
        this.UBICACION = UBICACION;
    }

    public void setDESCUBICACION(String DESCUBICACION) {
        this.DESCUBICACION = DESCUBICACION;
    }

    public void setCAT7(String CAT7) {
        this.CAT7 = CAT7;
    }

    public void setNUMCOMPA(String NUMCOMPA) {
        this.NUMCOMPA = NUMCOMPA;
    }

    public void setUSUASIGNADO(String USUASIGNADO) {
        this.USUASIGNADO = USUASIGNADO;
    }

    public void setCOSTO(String COSTO) {
        this.COSTO = COSTO;
    }

    public void setDEPACU(String DEPACU) {
        this.DEPACU = DEPACU;
    }

    public  Activo(){};
    //FIN DEVROD


    public Activo(String NUMCHAPA, String NUMACTIVO, String DESCRIPCION,
                  String ESTADO, String DESCESTADO, String UBICACION,
                  String DESCUBICACION, String CAT7, String NUMCOMPA,
                  String USUASIGNADO, String COSTO, String DEPACU) {
        this.NUMCHAPA = NUMCHAPA;
        this.NUMACTIVO = NUMACTIVO;
        this.DESCRIPCION = DESCRIPCION;
        this.ESTADO = ESTADO;
        this.DESCESTADO = DESCESTADO;
        this.UBICACION = UBICACION;
        this.DESCUBICACION = DESCUBICACION;
        this.CAT7 = CAT7;
        this.NUMCOMPA = NUMCOMPA;
        this.USUASIGNADO = USUASIGNADO;
        this.COSTO = COSTO;
        this.DEPACU = DEPACU;
    }


    public String getNUMCHAPA() {
        return NUMCHAPA;
    }

    public String getNUMACTIVO() {
        return NUMACTIVO;
    }

    public String getDESCRIPCION() {
        return DESCRIPCION;
    }

    public String getESTADO() {
        return ESTADO;
    }

    public String getDESCESTADO() {
        return DESCESTADO;
    }

    public String getUBICACION() {
        return UBICACION;
    }

    public String getDESCUBICACION() {
        return DESCUBICACION;
    }

    public String getCAT7() {
        return CAT7;
    }

    public String getNUMCOMPA() {
        return NUMCOMPA;
    }

    public String getUSUASIGNADO() {
        return USUASIGNADO;
    }

    public String getCOSTO() {
        return COSTO;
    }

    public String getDEPACU() {
        return DEPACU;
    }
}
