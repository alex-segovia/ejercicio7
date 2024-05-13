package com.example.ejercicio7.Controller;
import com.example.ejercicio7.Entity.Character;

import com.example.ejercicio7.Repository.CharacterRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class CharacterController {
    final CharacterRepository characterRepository;

    public CharacterController(CharacterRepository characterRepository){
        this.characterRepository = characterRepository;
    }

    @GetMapping(value = "/personaje/list")
    public String listar(Model model,
                         @RequestParam(value = "busqueda",required = false) String busqueda){
        List<Character> listaPersonajes;
        if(busqueda==null){
            listaPersonajes = characterRepository.findAll();
        }else{
            listaPersonajes = characterRepository.findByNameIgnoreCaseContaining('%'+busqueda+'%');
            model.addAttribute("busquedaRealizada",busqueda);
        }
        model.addAttribute("listaPersonajes",listaPersonajes);
        return "listar";
    }

    @GetMapping(value = "/personaje/new")
    public String crear(@ModelAttribute("character") Character character,
                        Model model){
        List<String> listaIdentidad = characterRepository.listarIdentity();
        List<String> listaAlineacion = characterRepository.listarAlineacion();
        List<String> listaOjos = characterRepository.listarOjos();
        List<String> listaCabello = characterRepository.listarCabello();
        List<String> listaSexo = characterRepository.listarSexo();
        List<String> listaGSM = characterRepository.listarGSM();
        List<String> listaAlive = characterRepository.listarAlive();

        model.addAttribute("listaIdentidad",listaIdentidad);
        model.addAttribute("listaAlineacion",listaAlineacion);
        model.addAttribute("listaOjos",listaOjos);
        model.addAttribute("listaCabello",listaCabello);
        model.addAttribute("listaSexo",listaSexo);
        model.addAttribute("listaGSM",listaGSM);
        model.addAttribute("listaAlive",listaAlive);

        model.addAttribute("tipo", "crear");
        return "formulario";
    }

    @GetMapping(value = "/personaje/edit")
    public String editar(@ModelAttribute("character") Character character,
                         Model model, @RequestParam("id") int id){
        Optional<Character> optionalCharacter = characterRepository.findById(id);
        if(optionalCharacter.isPresent()){
            character = optionalCharacter.get();

            List<String> listaIdentidad = characterRepository.listarIdentity();
            List<String> listaAlineacion = characterRepository.listarAlineacion();
            List<String> listaOjos = characterRepository.listarOjos();
            List<String> listaCabello = characterRepository.listarCabello();
            List<String> listaSexo = characterRepository.listarSexo();
            List<String> listaGSM = characterRepository.listarGSM();
            List<String> listaAlive = characterRepository.listarAlive();

            model.addAttribute("listaIdentidad",listaIdentidad);
            model.addAttribute("listaAlineacion",listaAlineacion);
            model.addAttribute("listaOjos",listaOjos);
            model.addAttribute("listaCabello",listaCabello);
            model.addAttribute("listaSexo",listaSexo);
            model.addAttribute("listaGSM",listaGSM);
            model.addAttribute("listaAlive",listaAlive);

            model.addAttribute("character", character);
            model.addAttribute("tipo", "editar");
            return "formulario";
        }else{
            return "redirect:/personaje/list";
        }
    }

    @PostMapping(value = "/personaje/save")
    public String guardar(@ModelAttribute("character") @Valid Character character, BindingResult bindingResult,
                          Model model, RedirectAttributes attr, @RequestParam("tipo") String tipo){
        if(bindingResult.hasErrors()){
            List<String> listaIdentidad = characterRepository.listarIdentity();
            List<String> listaAlineacion = characterRepository.listarAlineacion();
            List<String> listaOjos = characterRepository.listarOjos();
            List<String> listaCabello = characterRepository.listarCabello();
            List<String> listaSexo = characterRepository.listarSexo();
            List<String> listaGSM = characterRepository.listarGSM();
            List<String> listaAlive = characterRepository.listarAlive();

            model.addAttribute("listaIdentidad",listaIdentidad);
            model.addAttribute("listaAlineacion",listaAlineacion);
            model.addAttribute("listaOjos",listaOjos);
            model.addAttribute("listaCabello",listaCabello);
            model.addAttribute("listaSexo",listaSexo);
            model.addAttribute("listaGSM",listaGSM);
            model.addAttribute("listaAlive",listaAlive);

            model.addAttribute("tipo",tipo);
            return "formulario";
        }else{
            attr.addFlashAttribute("mensaje","Personaje " + (character.getId()==0?"creado ":"actualizado ") + "exitosamente");
            character.setIdentity(character.getIdentity().equals("0")?null:character.getIdentity());
            character.setAlign(character.getAlign().equals("0")?null:character.getAlign());
            character.setEye(character.getEye().equals("0")?null:character.getEye());
            character.setHair(character.getHair().equals("0")?null:character.getHair());
            character.setSex(character.getSex().equals("0")?null:character.getSex());
            character.setGsm(character.getGsm().equals("0")?null:character.getGsm());
            character.setAlive(character.getAlive().equals("0")?null:character.getAlive());
            characterRepository.save(character);
            return "redirect:/personaje/list";
        }
    }

    @GetMapping(value = "/personaje/delete")
    public String borrar(Model model,
                         @RequestParam("id") int id,
                         RedirectAttributes attr){
        Optional<Character> optionalCharacter = characterRepository.findById(id);
        if(optionalCharacter.isPresent()){
            characterRepository.deleteById(id);
            attr.addFlashAttribute("mensaje","Personaje borrado exitosamente");
        }
        return "redirect:/personaje/list";
    }
}