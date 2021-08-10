package tads.ufrn.eaj.loja.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tads.ufrn.eaj.loja.model.Joia;
import tads.ufrn.eaj.loja.service.FileStorageService;
import tads.ufrn.eaj.loja.service.JoiaService;

import javax.validation.Valid;


@Controller
public class JoiaController{
    JoiaService service;
    FileStorageService fileStorageService;

    @Autowired
    public void setFileStorageService(FileStorageService fileStorageService){
        this.fileStorageService = fileStorageService;
    }


    @Autowired
    public void setService(JoiaService service) {
        this.service = service;
    }

    @RequestMapping("/cadastro")
    public String getFormCadastro(Model model){
        Joia joia = new Joia();
        model.addAttribute("joia", joia);
        return "cadastro";

    }

    @RequestMapping(value = "/salvar", method = RequestMethod.POST)
    public String salvar(@ModelAttribute @Valid Joia joia, Errors errors, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes){

        if(errors.hasErrors()) {
            return "cadastro";
        }else{
            joia.setImageUri(file.getOriginalFilename());
            service.save(joia);
            fileStorageService.save(file);
            redirectAttributes.addAttribute("mensagem", "Cadastro realizado com sucesso");
            return "redirect:/admin";

        }

    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String listAllJoia2(Model model){
        var listJoias = service.listAll();
        model.addAttribute("listJoia2", listJoias);
        return "admin";

    }

    @RequestMapping("/editar/{id}")
    public ModelAndView getFormEdit(@PathVariable(name = "id") Long id){
        ModelAndView modelAndView = new ModelAndView("editar");
        Joia joia = service.findById(id);
        modelAndView.addObject("formJoia", joia);
        return modelAndView;
    }

    @RequestMapping(value = "/deletar/{id}", method = RequestMethod.GET)
    public String deletar(@PathVariable(name = "id") Long id, RedirectAttributes redirectAttributes){
        service.deletar(id);
        redirectAttributes.addAttribute("mensagem", "Produto excluido com sucesso!");
        return "redirect:/admin";
    }

}
