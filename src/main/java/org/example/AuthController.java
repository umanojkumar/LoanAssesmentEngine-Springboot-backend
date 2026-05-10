package org.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final DataRepository dataRepository;

    @Value("${app.upload-dir:uploads}")
    private String uploadDir;

    public AuthController(AuthService authService, JwtUtil jwtUtil, UserRepository userRepository, DataRepository dataRepository) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.dataRepository = dataRepository;
    }

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");
        if (email == null || password == null) return ResponseEntity.badRequest().body("email and password required");
        User u = authService.register(email, password);
        return ResponseEntity.ok(Map.of("id", u.getId(), "email", u.getEmail()));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");
        if (authService.checkCredentials(email, password)) {
            String token = jwtUtil.generateToken(email);
            return ResponseEntity.ok(Map.of("token", token));
        }
        return ResponseEntity.status(401).body("invalid credentials");
    }

    @PostMapping(value = "/data/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveData(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Map<String, Object> body) {
        if (userDetails == null) return ResponseEntity.status(401).build();

        String businessName = readString(body, "businessName");
        String requestedAmount = readString(body, "requestedAmount");
        if (isBlank(businessName)) return ResponseEntity.badRequest().body("businessName required");
        if (isBlank(requestedAmount)) return ResponseEntity.badRequest().body("requestedAmount required");

        String validationError = validateNumber(body, "yearsInBusiness", 0);
        if (validationError != null) return ResponseEntity.badRequest().body(validationError);
        validationError = validateNumber(body, "annualRevenue", null);
        if (validationError != null) return ResponseEntity.badRequest().body(validationError);
        validationError = validateNumber(body, "monthlyExpenses", null);
        if (validationError != null) return ResponseEntity.badRequest().body(validationError);
        validationError = validateNumber(body, "profitEstimate", null);
        if (validationError != null) return ResponseEntity.badRequest().body(validationError);
        validationError = validateNumber(body, "existingEmi", null);
        if (validationError != null) return ResponseEntity.badRequest().body(validationError);
        validationError = validateNumber(body, "requestedAmount", null);
        if (validationError != null) return ResponseEntity.badRequest().body(validationError);
        validationError = validateNumber(body, "tenure", 1);
        if (validationError != null) return ResponseEntity.badRequest().body(validationError);

        DataEntity d = new DataEntity();
        d.setOwner(userDetails.getUsername());
        d.setBusinessName(businessName);
        d.setBusinessType(readString(body, "businessType"));
        d.setGstPan(readString(body, "gstPan"));
        d.setIndustry(readString(body, "industry"));
        d.setYearsInBusiness(readString(body, "yearsInBusiness"));
        d.setAnnualRevenue(readString(body, "annualRevenue"));
        d.setMonthlyExpenses(readString(body, "monthlyExpenses"));
        d.setProfitEstimate(readString(body, "profitEstimate"));
        d.setExistingEmi(readString(body, "existingEmi"));
        d.setRequestedAmount(requestedAmount);
        d.setPurpose(readString(body, "purpose"));
        d.setTenure(readString(body, "tenure"));
        d.setBankStatements(attachmentStatus(body, "bankStatements"));
        d.setGstReturns(attachmentStatus(body, "gstReturns"));
        d.setItrFiles(attachmentStatus(body, "itrFiles"));
        d.setBalanceSheetFiles(attachmentStatus(body, "balanceSheetFiles"));
        d.setLoanDocuments(attachmentStatus(body, "loanDocuments"));
        dataRepository.save(d);
        return ResponseEntity.ok(Map.of("id", d.getId()));
    }

    @PostMapping(value = "/data/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> saveMultipartData(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam Map<String, String> body,
            @RequestParam(required = false) MultiValueMap<String, MultipartFile> files
    ) {
        if (userDetails == null) return ResponseEntity.status(401).build();

        String businessName = readString(body, "businessName");
        String requestedAmount = readString(body, "requestedAmount");
        if (isBlank(businessName)) return ResponseEntity.badRequest().body("businessName required");
        if (isBlank(requestedAmount)) return ResponseEntity.badRequest().body("requestedAmount required");

        String validationError = validateNumber(body, "yearsInBusiness", 0);
        if (validationError != null) return ResponseEntity.badRequest().body(validationError);
        validationError = validateNumber(body, "annualRevenue", null);
        if (validationError != null) return ResponseEntity.badRequest().body(validationError);
        validationError = validateNumber(body, "monthlyExpenses", null);
        if (validationError != null) return ResponseEntity.badRequest().body(validationError);
        validationError = validateNumber(body, "profitEstimate", null);
        if (validationError != null) return ResponseEntity.badRequest().body(validationError);
        validationError = validateNumber(body, "existingEmi", null);
        if (validationError != null) return ResponseEntity.badRequest().body(validationError);
        validationError = validateNumber(body, "requestedAmount", null);
        if (validationError != null) return ResponseEntity.badRequest().body(validationError);
        validationError = validateNumber(body, "tenure", 1);
        if (validationError != null) return ResponseEntity.badRequest().body(validationError);

        DataEntity d = new DataEntity();
        d.setOwner(userDetails.getUsername());
        d.setBusinessName(businessName);
        d.setBusinessType(readString(body, "businessType"));
        d.setGstPan(readString(body, "gstPan"));
        d.setIndustry(readString(body, "industry"));
        d.setYearsInBusiness(readString(body, "yearsInBusiness"));
        d.setAnnualRevenue(readString(body, "annualRevenue"));
        d.setMonthlyExpenses(readString(body, "monthlyExpenses"));
        d.setProfitEstimate(readString(body, "profitEstimate"));
        d.setExistingEmi(readString(body, "existingEmi"));
        d.setRequestedAmount(requestedAmount);
        d.setPurpose(readString(body, "purpose"));
        d.setTenure(readString(body, "tenure"));
        d.setBankStatements("no");
        d.setGstReturns("no");
        d.setItrFiles("no");
        d.setBalanceSheetFiles("no");
        d.setLoanDocuments("no");

        dataRepository.save(d);

        try {
            Path entryDir = Paths.get(uploadDir, d.getId().toString());
            d.setBankStatements(saveAttachmentGroup(files, "bankStatements", entryDir) ? "yes" : "no");
            d.setGstReturns(saveAttachmentGroup(files, "gstReturns", entryDir) ? "yes" : "no");
            d.setItrFiles(saveAttachmentGroup(files, "itrFiles", entryDir) ? "yes" : "no");
            d.setBalanceSheetFiles(saveAttachmentGroup(files, "balanceSheetFiles", entryDir) ? "yes" : "no");
            d.setLoanDocuments(saveAttachmentGroup(files, "loanDocuments", entryDir) ? "yes" : "no");
            dataRepository.save(d);
        } catch (IOException ex) {
            return ResponseEntity.internalServerError().body("failed to save attachments");
        }

        return ResponseEntity.ok(Map.of("id", d.getId()));
    }

    private String readString(Map<String, ?> body, String key) {
        Object value = body.get(key);
        if (value == null) return null;
        String text = value.toString().trim();
        return text.isEmpty() ? null : text;
    }

    private String validateNumber(Map<String, ?> body, String key, Integer minValue) {
        String value = readString(body, key);
        if (value == null) return null;
        try {
            double number = Double.parseDouble(value);
            if (minValue != null && number < minValue) {
                return key + " must be at least " + minValue;
            }
        } catch (NumberFormatException ex) {
            return key + " must be a number";
        }
        return null;
    }

    private boolean saveAttachmentGroup(MultiValueMap<String, MultipartFile> files, String key, Path entryDir) throws IOException {
        List<MultipartFile> attachmentFiles = filesFor(files, key);
        if (attachmentFiles.isEmpty()) return false;

        boolean savedAny = false;
        Path groupDir = entryDir.resolve(key);
        for (MultipartFile file : attachmentFiles) {
            if (file == null || file.isEmpty()) continue;
            Files.createDirectories(groupDir);
            String filename = cleanFilename(file.getOriginalFilename());
            Path target = uniqueTarget(groupDir, filename);
            file.transferTo(target);
            savedAny = true;
        }
        return savedAny;
    }

    private List<MultipartFile> filesFor(MultiValueMap<String, MultipartFile> files, String key) {
        if (files == null) return List.of();
        List<MultipartFile> nestedFiles = files.get("attachments." + key);
        if (nestedFiles != null) return nestedFiles;
        List<MultipartFile> directFiles = files.get(key);
        return directFiles == null ? List.of() : directFiles;
    }

    private String cleanFilename(String originalFilename) {
        if (isBlank(originalFilename)) return UUID.randomUUID().toString();
        Path filename = Paths.get(originalFilename).getFileName();
        return filename == null ? UUID.randomUUID().toString() : filename.toString();
    }

    private Path uniqueTarget(Path directory, String filename) {
        Path target = directory.resolve(filename);
        if (!Files.exists(target)) return target;

        String suffix = "-" + UUID.randomUUID();
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex <= 0) return directory.resolve(filename + suffix);
        return directory.resolve(filename.substring(0, dotIndex) + suffix + filename.substring(dotIndex));
    }

    @SuppressWarnings("unchecked")
    private String attachmentStatus(Map<String, Object> body, String key) {
        Object attachments = body.get("attachments");
        Object value = body.get("attachments." + key);
        if (attachments instanceof Map<?, ?> attachmentMap) {
            value = ((Map<String, Object>) attachmentMap).getOrDefault(key, value);
        }
        return hasAttachmentValue(value) ? "yes" : "no";
    }

    private boolean hasAttachmentValue(Object value) {
        if (value == null) return false;
        if (value instanceof Collection<?> collection) return !collection.isEmpty();
        if (value.getClass().isArray()) return Array.getLength(value) > 0;
        return !value.toString().trim().isEmpty();
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
