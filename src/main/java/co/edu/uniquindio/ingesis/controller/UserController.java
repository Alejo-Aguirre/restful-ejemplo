package co.edu.uniquindio.ingesis.controller;

import co.edu.uniquindio.ingesis.model.User;
import co.edu.uniquindio.ingesis.service.UserService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponseSchema;

import java.util.List;

@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Gestión de Usuarios", description = "API para gestionar usuarios")
public class UserController {

    @Inject
    UserService userService;

    @POST
    @Operation(summary = "Registra un nuevo usuario", description = "Crea un nuevo usuario en el sistema.")
    @APIResponse(responseCode = "201", description = "Usuario registrado exitosamente")
    @APIResponseSchema(User.class)
    public Response registerUser(User user) {
        User registeredUser = userService.registerUser(user);
        return Response.status(Response.Status.CREATED).entity(registeredUser).build();
    }

    @GET
    @Operation(summary = "Obtiene la lista de todos los usuarios")
    @APIResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente")
    @APIResponseSchema(List.class)
    public Response getAllUsers() {
        List<User> users = userService.getAllUsers();
        return Response.ok(users).build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Obtiene un usuario por ID")
    @APIResponse(responseCode = "200", description = "Usuario encontrado")
    @APIResponse(responseCode = "404", description = "Usuario no encontrado")
    @APIResponseSchema(User.class)
    public Response getUserById(@Parameter(description = "ID del usuario a consultar") @PathParam("id") Integer id) {
        User user = userService.getUserById(id).orElse(null);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(user).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Actualiza un usuario existente (completo)")
    @APIResponse(responseCode = "200", description = "Usuario actualizado correctamente")
    public Response updateUser(@PathParam("id") Integer id, User user) {
        userService.updateUser(id, user);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Elimina un usuario por ID")
    @APIResponse(responseCode = "204", description = "Usuario eliminado correctamente")
    public Response deleteUser(@PathParam("id") Integer id) {
        userService.deleteUser(id);
        return Response.noContent().build();
    }
}
