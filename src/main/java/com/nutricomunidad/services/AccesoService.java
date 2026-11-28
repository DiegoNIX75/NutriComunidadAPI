package com.nutricomunidad.services;

import com.nutricomunidad.dto.UsuarioToken;
import com.nutricomunidad.errors.Errores;
import com.nutricomunidad.models.Beneficiario;
import com.nutricomunidad.models.Comercio;
import com.nutricomunidad.models.Organizacion;
import com.nutricomunidad.repositories.BeneficiarioRepository;
import com.nutricomunidad.repositories.ComercioRepository;
import com.nutricomunidad.repositories.OrganizacionRepository;
import com.nutricomunidad.security.Roles;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * Reglas de acceso por propietario. Un usuario con rol Beneficiario, Comercio u Organizacion sólo
 * puede operar sobre el registro vinculado a su cuenta (columna id_usuario). Administrador y los
 * demás roles conservan los permisos definidos por {@code @RequiereRol}.
 *
 * <p>Las verificaciones {@code exigir*} sólo actúan cuando el registro referenciado existe; si no
 * existe, el servicio correspondiente responde 400/404 como siempre.
 */
@Service
public class AccesoService {

    private static final String SOLO_PROPIOS = "Solo puedes acceder a los datos vinculados a tu cuenta";

    private final BeneficiarioRepository beneficiarios;
    private final ComercioRepository comercios;
    private final OrganizacionRepository organizaciones;

    public AccesoService(BeneficiarioRepository beneficiarios, ComercioRepository comercios,
                         OrganizacionRepository organizaciones) {
        this.beneficiarios = beneficiarios;
        this.comercios = comercios;
        this.organizaciones = organizaciones;
    }

    public static boolean tieneRol(UsuarioToken usuario, String rol) {
        return usuario != null && rol.equalsIgnoreCase(usuario.rol());
    }

    public Optional<Beneficiario> beneficiarioDe(UsuarioToken usuario) {
        return beneficiarios.buscarPorUsuario(usuario.id());
    }

    public Optional<Comercio> comercioDe(UsuarioToken usuario) {
        return comercios.buscarPorUsuario(usuario.id());
    }

    public Optional<Organizacion> organizacionDe(UsuarioToken usuario) {
        return organizaciones.buscarPorUsuario(usuario.id());
    }

    /** Id del beneficiario propio, o -1 si la cuenta aún no tiene uno (para filtrar listados). */
    public int idBeneficiarioOSinResultados(UsuarioToken usuario) {
        return beneficiarioDe(usuario).map(Beneficiario::idBeneficiario).orElse(-1);
    }

    public int idComercioOSinResultados(UsuarioToken usuario) {
        return comercioDe(usuario).map(Comercio::idComercio).orElse(-1);
    }

    public int idOrganizacionOSinResultados(UsuarioToken usuario) {
        return organizacionDe(usuario).map(Organizacion::idOrganizacion).orElse(-1);
    }

    public void exigirBeneficiario(UsuarioToken usuario, Integer idBeneficiario) {
        if (tieneRol(usuario, Roles.BENEFICIARIO) && beneficiarios.existe(idBeneficiario)
                && idBeneficiarioOSinResultados(usuario) != idBeneficiario) {
            throw Errores.prohibido(SOLO_PROPIOS);
        }
    }

    public void exigirComercio(UsuarioToken usuario, Integer idComercio) {
        if (tieneRol(usuario, Roles.COMERCIO) && comercios.existe(idComercio)
                && idComercioOSinResultados(usuario) != idComercio) {
            throw Errores.prohibido(SOLO_PROPIOS);
        }
    }

    public void exigirOrganizacion(UsuarioToken usuario, Integer idOrganizacion) {
        if (tieneRol(usuario, Roles.ORGANIZACION) && organizaciones.existe(idOrganizacion)
                && idOrganizacionOSinResultados(usuario) != idOrganizacion) {
            throw Errores.prohibido(SOLO_PROPIOS);
        }
    }

    /**
     * Cuenta propietaria para un alta: si quien crea el registro tiene el rol de esa entidad
     * (p. ej. un Comercio que registra su comercio), el registro queda vinculado a su cuenta y sólo
     * se permite uno por cuenta. Si lo crea otro rol (Administrador, Organización), queda sin vínculo.
     */
    public Integer propietarioParaAlta(UsuarioToken usuario, String rolEntidad, boolean yaTieneUno) {
        if (!tieneRol(usuario, rolEntidad)) {
            return null;
        }
        if (yaTieneUno) {
            throw Errores.conflicto("Tu cuenta ya tiene un registro vinculado; actualízalo en lugar de crear otro");
        }
        return usuario.id();
    }
}
