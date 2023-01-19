package com.bb.focus.db.entity.process;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name="process_applicants")
public class ProcessApplicant {


    @Id
    @GeneratedValue
    @Column(name="process_applicants_id")
    private int processApplicantId;

    @ManyToOne
    @JoinColumn(name="applicant_id")
    private applicant applicantId;

    @ManyToOne
    @JoinColumn(name="process_id")
    private Process procesId;




}
