package io.github.Guimaraes131.vroom.motorcycle;

import io.github.Guimaraes131.vroom.motorcycle.dto.MotorcycleForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/motorcycle")
public class MotorcycleController {

    private final MotorcycleService motorcycleService;

    @GetMapping("/form")
    public String createMotorcycleForm(@RequestParam(required = false) UUID tagId, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        MotorcycleForm form = new MotorcycleForm();
        form.setTagId(tagId);

        model.addAttribute("username", username);
        model.addAttribute("motorcycleForm", form);

        return "form";
    }

    @GetMapping("/{id}")
    public String motorcycleDetails(@PathVariable UUID id, Model model) {
        Motorcycle motorcycle = motorcycleService.getById(id);

        model.addAttribute("motorcycle", motorcycle);
        return "motorcycle-details";
    }

    @GetMapping("/update/{id}")
    public String updateMotorcycleForm(@PathVariable UUID id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Motorcycle motorcycle = motorcycleService.getById(id);
        MotorcycleForm form = MotorcycleForm.builder()
                .chassis(motorcycle.getChassis())
                .licensePlate(motorcycle.getLicensePlate())
                .model(motorcycle.getModel())
                .problem(motorcycle.getProblem())
                .tagId(motorcycle.getTag().getId())
                .build();

        model.addAttribute("username", username);
        model.addAttribute("motorcycleForm", form);
        model.addAttribute("motorcycleId", id);

        return "update-motorcycle-form";
    }

    @PostMapping("/form")
    public String create(@Valid MotorcycleForm motorcycleForm, BindingResult result, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        model.addAttribute("username", username);

        if (result.hasErrors()) return "form";

        motorcycleService.create(motorcycleService.toEntity(motorcycleForm));
        return "redirect:/tags";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable UUID id,
                         @ModelAttribute("motorcycleForm") @Valid MotorcycleForm motorcycleForm,
                         BindingResult result,
                         Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        model.addAttribute("username", username);
        model.addAttribute("motorcycleId", id);

        if (result.hasErrors()) return "update-motorcycle-form";

        motorcycleService.update(id, motorcycleForm);
        return "redirect:/motorcycle/" + id;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable UUID id) {
        motorcycleService.delete(id);

        return "redirect:/tags";
    }
}
