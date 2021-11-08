package com.backblog.web.admin;

import com.backblog.po.Type;
import com.backblog.po.Tag;
import com.backblog.service.TagService;
import com.backblog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;


@Controller
@RequestMapping("/admin")
public class TypeTagAdminController {

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;


    @GetMapping("/sort_label")
    public String typesAndTags(@PageableDefault(size = 5, sort = {"id"}, direction = Sort.Direction.DESC)
                                 Pageable pageable, Model model) {
        model.addAttribute("sorts", typeService.ListType());
        model.addAttribute("page", tagService.ListTag(pageable));
        return "admin/sort_label";
    }

    @GetMapping("/sort_label/input_type")
    public String inputType() {
        return "admin/sort_input";
    }

    @GetMapping("/sort_label/input_tag")
    public String inputTag() {
        return "admin/label_input";
    }

    @GetMapping("/sort_label/{id}/edit_type")
    public String editType(@PathVariable Long id, Model model) {
        model.addAttribute("type", typeService.getType(id));
        return "admin/sort_edit";
    }

    @GetMapping("/sort_label/{id}/edit_tag")
    public String editTag(@PathVariable Long id, Model model) {
        model.addAttribute("tag", tagService.getTag(id));
        return "admin/label_edit";
    }

    @PostMapping("/sort_label/sort")
    public String postType(Type type, RedirectAttributes attributes) {
        Type type1 = typeService.findByName(type.getName());
        if (Objects.equals(type.getName(), "") || type.getName() == null || type1 != null) {
            attributes.addFlashAttribute("msg", "Add type failed, check if name is empty or exists.");
        } else {
            typeService.save(type);
            return "redirect:/admin/sort_label";
        }
        return "redirect:/admin/sort_label/input_type";
    }

    @PostMapping("/sort_label/label")
    public String postTag(Tag tag, RedirectAttributes attributes) {
        Tag tag1 = tagService.findByName(tag.getName());
        if (Objects.equals(tag.getName(), "") || tag.getName() == null || tag1 != null) {
            attributes.addFlashAttribute("msg", "Add tag failed, check if name is empty or exists.");
        } else {
            tagService.save(tag);
            return "redirect:/admin/sort_label";
        }
        return "redirect:/admin/sort_label/input_tag";
    }

    @PostMapping("/sort_label/sort/{id}")
    public String editPostType(Type type, @PathVariable Long id, RedirectAttributes attributes) {
        Type type1 = typeService.findByName(type.getName());
        if (Objects.equals(type.getName(), "") || type.getName() == null || type1 != null) {
            attributes.addFlashAttribute("msg", "Edit type failed, check if name is empty or exists.");
        } else {
            typeService.updateType(id, type);
            return "redirect:/admin/sort_label";
        }

        return "redirect:/admin/sort_label/input_type";
    }

    @PostMapping("/sort_label/label/{id}")
    public String editPostTag(Tag tag, @PathVariable Long id, RedirectAttributes attributes) {
        Tag tag1 = tagService.findByName(tag.getName());
        if (Objects.equals(tag.getName(), "") || tag.getName() == null || tag1 != null) {
            attributes.addFlashAttribute("msg", "Edit tag failed, check if name is empty or exists.");
        } else {
            tagService.updateTag(id, tag);
            return "redirect:/admin/sort_label";
        }

        return "redirect:/admin/sort_label/input_tag";
    }

    @GetMapping("/sort_label/{id}/delete_type")
    public String deleteType(@PathVariable Long id) {
        typeService.deleteType(id);
        return "redirect:/admin/sort_label";
    }

    @GetMapping("/sort_label/{id}/delete_tag")
    public String deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        return "redirect:/admin/sort_label";
    }
}
