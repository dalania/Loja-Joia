package tads.ufrn.eaj.loja.controller;

import javax.servlet.http.Cookie;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


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
    public String salvar(@ModelAttribute @Valid Joia joia, Errors errors, @RequestParam("file") MultipartFile file , RedirectAttributes redirectAttributes, @RequestParam String previousPage ){
        if(errors.hasErrors()) {
            return "cadastro";
        }else{
            if(file.isEmpty()){
                String image = this.service.findById(joia.getId()).getImageUri();
                joia.setImageUri(image);
            }else{
                Integer hascode = new Integer(hashCode());
                joia.setImageUri(hascode + file.getOriginalFilename());
                fileStorageService.save(hascode, file);
            }
            service.save(joia);
            redirectAttributes.addAttribute("mensagem", "Cadastro realizado com sucesso");
            String message = previousPage.equals("cadastro") ? "Cadastrado" : "Editado";
            return "redirect:/admin?message=" + message;
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
        return "redirect:/admin?message=Deletado";
    }

    @RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
    public String getJoias(Model model, HttpServletResponse response){

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss:SSS");
        Date date = new Date();
        Cookie cookie =  new Cookie ("visita", dateFormat.format(date));
        cookie.setMaxAge(60*60*24); //expira em 24 horas
        response.addCookie(cookie); //adicionando o cookie a resposta

        var listJoias = service.listAll();
        model.addAttribute("listJoiaClient", listJoias);
        return "index";
    }

    @RequestMapping("/adicionarCarrinho/{id}")
    public String getCarrinho(@PathVariable(name = "id") Long id, HttpServletRequest request){
        System.out.println(service.findById(id));

        HttpSession session = request.getSession();

        if(session.getAttribute("carrinho")==null){
            session.setAttribute("carrinho", new ArrayList<Joia>());
        }
        ArrayList<Joia> productsCar = (ArrayList<Joia>) session.getAttribute("carrinho");

        productsCar.add(service.findById(id));
        System.out.println("");
        return "redirect:/";

    }
    @RequestMapping("/verCarrinho")
    public String getCarrinho(Model model, HttpServletRequest request){
        System.out.println(service.listAll());
        HttpSession session = request.getSession();
        if(session.getAttribute("carrinho")!=null){
            ArrayList<Joia> arrayProduct = (ArrayList<Joia>) session.getAttribute("carrinho");
            model.addAttribute("listCar",arrayProduct);
            System.out.println(arrayProduct);
            return "carrinho";

        }
        return "redirect:/?message=Não existe produtos no carrinho";

    }

    @RequestMapping("/finalizarCompra")
    public  String finalizarCompra(HttpServletRequest request){
        request.getSession().invalidate();
        return "redirect:/";
    }

}
